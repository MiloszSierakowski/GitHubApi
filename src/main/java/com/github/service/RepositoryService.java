package com.github.service;

import com.github.controller.GithubApiController;
import com.github.domain.Branch;
import com.github.domain.BranchDto;
import com.github.domain.RepositoryDto;
import com.github.domain.RepositoryInformationDto;
import com.github.exceptions.UserNotFoundException;
import com.github.mapper.BranchMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RepositoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GithubApiController.class);
    private final RestTemplate restTemplate;
    private final BranchMapper branchMapper;

    public List<RepositoryDto> getUserRepository(String userName) throws UserNotFoundException {
        URI url = uriBuilder(userName);
        try {
            RepositoryDto[] boardsResponse = restTemplate.getForObject(url, RepositoryDto[].class);

            return Optional.ofNullable(boardsResponse)
                    .map(Arrays::asList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .filter(x -> !x.isFork())
                    .collect(Collectors.toList());
        } catch (HttpClientErrorException.NotFound ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new UserNotFoundException();
        }catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<RepositoryInformationDto> createListOfRepositoryInformationDto(List<RepositoryDto> repositoryDtoList) {
        List<RepositoryInformationDto> informationDtoList = new ArrayList<>();

        for (RepositoryDto rep : repositoryDtoList) {
            String url = rep.getBranches_url().replace("{/branch}", "");
            BranchDto[] branchDtos;
            List<Branch> branchList;
            try {
                branchDtos = restTemplate.getForObject(url, BranchDto[].class);
                branchList = Optional.ofNullable(branchDtos)
                        .map(Arrays::asList)
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(branchMapper::mapToBranch)
                        .toList();

            } catch (RestClientException e) {
                LOGGER.error(e.getMessage(), e);
                branchList = new ArrayList<>();
            }
            informationDtoList.add(new RepositoryInformationDto(rep.getName(), rep.getOwnerDto().getLogin(), branchList));
        }
        return informationDtoList;
    }

    private URI uriBuilder(String userName) {
        return UriComponentsBuilder.fromHttpUrl("https://api.github.com/users/" + userName + "/repos")
                .build()
                .encode()
                .toUri();
    }

}
