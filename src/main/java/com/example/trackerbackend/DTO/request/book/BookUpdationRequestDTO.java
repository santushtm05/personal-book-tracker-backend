package com.example.trackerbackend.DTO.request.book;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Builder
public class BookUpdationRequestDTO {
    private Integer id;
    private String title;
    private String author;
    private String status;
    private Float rating;
    private Integer pages;
    private Set<Integer> tagIds;
    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;
    @JsonProperty("started_at")
    private LocalDateTime startedAt;
    @JsonProperty("completed_at")
    private LocalDateTime completedAt;
}