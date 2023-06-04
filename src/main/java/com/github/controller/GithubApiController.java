package com.github.controller;

import com.github.domain.RepositoryDto;
import com.github.domain.RepositoryInformationDto;
import com.github.exceptions.UnsupportedMediaType;
import com.github.exceptions.UserNotFoundException;
import com.github.service.RepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/github")
@RequiredArgsConstructor
@CrossOrigin("*")
public class GithubApiController {
    private final RepositoryService repositoryService;

    @GetMapping(value = "repositories/{userName}")
    public ResponseEntity<Object> getListOfUserRepository(@PathVariable String userName, @RequestHeader("Accept") String accept) throws UserNotFoundException, UnsupportedMediaType {
        if (accept.equals("application/xml")) {
            throw new UnsupportedMediaType();
        }
        List<RepositoryDto> repositoryDtoList = repositoryService.getUserRepository(userName);
        List<RepositoryInformationDto> repositoryInformationDtoList = repositoryService.createListOfRepositoryInformationDto(repositoryDtoList);
        return ResponseEntity.ok(repositoryInformationDtoList);
    }

}
