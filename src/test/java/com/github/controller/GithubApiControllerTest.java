package com.github.controller;

import com.github.domain.Branch;
import com.github.domain.OwnerDto;
import com.github.domain.RepositoryDto;
import com.github.domain.RepositoryInformationDto;
import com.github.exceptions.UserNotFoundException;
import com.github.service.RepositoryService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(GithubApiController.class)
class GithubApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RepositoryService repositoryService;

    @Test
    void getListOfUserRepositoryTest() throws Exception {
        List<RepositoryDto> repositoryDtoList = List.of(
                new RepositoryDto("Kalkulator", new OwnerDto("MiloszSierakowski"), true, "branches_url"),
                new RepositoryDto("TicTakToe", new OwnerDto("MiloszSierakowski"), true, "branches_url")
        );
        List<RepositoryInformationDto> repositoryInformationDtoList = List.of(
                new RepositoryInformationDto(repositoryDtoList.get(0).getName(), repositoryDtoList.get(0).getOwnerDto().getLogin(),
                        List.of(
                                new Branch("master", "123"),
                                new Branch("branch1", "123333")
                        )),
                new RepositoryInformationDto(repositoryDtoList.get(1).getName(), repositoryDtoList.get(1).getOwnerDto().getLogin(),
                        List.of(
                                new Branch("master", "567")
                        ))
        );

        when(repositoryService.getUserRepository(any(String.class))).thenReturn(repositoryDtoList);
        when(repositoryService.createListOfRepositoryInformationDto(repositoryDtoList)).thenReturn(repositoryInformationDtoList);

        mockMvc.
                perform(MockMvcRequestBuilders
                        .get("/v1/github/repositories/MiloszSierakowski")
                        .header("Accept","application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].repositoryName", Matchers.is("Kalkulator")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userLogin", Matchers.is("MiloszSierakowski")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].branchList[0].branchName", Matchers.is("master")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].branchList[1].branchName", Matchers.is("branch1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].branchList[0].lastCommitSha", Matchers.is("123")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].branchList[1].lastCommitSha", Matchers.is("123333")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].repositoryName", Matchers.is("TicTakToe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].userLogin", Matchers.is("MiloszSierakowski")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].branchList[0].branchName", Matchers.is("master")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].branchList[0].lastCommitSha", Matchers.is("567")));
    }

    @Test
    void getListOfUserRepositoriesThrowUserNotFoundException() throws Exception {
        when(repositoryService.getUserRepository(any(String.class))).thenThrow(new UserNotFoundException());

        mockMvc.
                perform(MockMvcRequestBuilders
                        .get("/v1/github/repositories/MiloszSierakowski")
                        .header("Accept","application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(HttpStatus.NOT_FOUND.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("User not found")));
    }

    @Test
    void unsupportedMediaTypeException() throws Exception {

        mockMvc.
                perform(MockMvcRequestBuilders
                        .get("/v1/github/repositories/MiloszSierakowski")
                        .header("Accept","application/xml")
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().is(406))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(HttpStatus.NOT_ACCEPTABLE.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Xml media format is not acceptable")));
    }
}
