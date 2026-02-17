package com.example.trackerbackend.DTO.response;

import com.example.trackerbackend.entity.Tag;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    @JsonProperty("id")
    private Integer id;
    private String title;
    private String author;
    private String status;
    private Float rating;
    private Integer pages;
    private String description;
    @JsonProperty("tags")
    private Set<TagDTO> tags;
    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;
    @JsonProperty("completed_at")
    private LocalDateTime completedAt;
}