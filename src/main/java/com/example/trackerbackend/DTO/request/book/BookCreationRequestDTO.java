package com.example.trackerbackend.DTO.request.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCreationRequestDTO {
    private String title;
    private String author;
    private String status;
    private Float rating;
    private Integer pages;
    private String thumbnailUrl;
    @JsonProperty("tags")
    private List<Integer> tagIds;
}