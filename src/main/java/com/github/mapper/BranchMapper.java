package com.github.mapper;

import com.github.domain.Branch;
import com.github.domain.BranchDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    @Mapping(source = "name", target = "branchName")
    @Mapping(source = "commitDto.sha", target = "lastCommitSha")
    Branch mapToBranch(BranchDto branchDto);

    @InheritInverseConfiguration(name = "mapToBranch")
    BranchDto mapToBranchDto(Branch branch);
}
