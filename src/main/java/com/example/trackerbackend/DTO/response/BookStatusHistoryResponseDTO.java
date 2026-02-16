package com.example.trackerbackend.DTO.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookStatusHistoryResponseDTO {

    private Integer id;
    private Integer bookId;
    private String oldStatus;
    private String newStatus;
    private LocalDateTime changedAt;
}
