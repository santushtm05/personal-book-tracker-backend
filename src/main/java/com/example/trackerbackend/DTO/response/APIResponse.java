package com.example.trackerbackend.DTO.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class APIResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private Object error;
    private LocalDateTime timestamp;
}