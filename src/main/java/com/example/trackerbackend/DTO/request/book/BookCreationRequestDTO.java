
package com.example.trackerbackend.DTO.request.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCreationRequestDTO {

    @NotBlank(message = "Title is required")
    @Size(max = 255)
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 255)
    private String author;

    @NotBlank(message = "Status is required")
    private String status;

    @NotNull(message = "Pages are required")
    @Min(value = 1, message = "Pages must be greater than 0")
    private Integer pages;

    @Min(0)
    @Max(5)
    private Float rating;

    @Size(max = 512)
    private String description;

    @Size(max = 512)
    private String thumbnailUrl;

    @JsonProperty("started_at")
    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    @JsonProperty("tags")
    private Set<Integer> tagIds;
}