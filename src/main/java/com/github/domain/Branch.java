package com.github.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Branch {
    private final String branchName;
    private final String lastCommitSha;
}
