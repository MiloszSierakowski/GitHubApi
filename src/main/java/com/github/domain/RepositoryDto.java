package com.github.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepositoryDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("owner")
    private OwnerDto ownerDto;

    @JsonProperty("fork")
    private boolean fork;

    @JsonProperty("branches_url")
    private String branches_url;

}
