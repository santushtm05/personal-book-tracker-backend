package com.example.trackerbackend.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TagDTO {
    private Integer id;
    @JsonProperty("tag_name")
    private String tagName;
}