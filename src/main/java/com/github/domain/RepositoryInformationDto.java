package com.github.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RepositoryInformationDto {

    private String repositoryName;
    private String userLogin;
    private List<Branch> branchList;

}
