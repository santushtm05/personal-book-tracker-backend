package com.example.trackerbackend.DTO.response.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class LoginResponse {
    private String token;
    private String username;
    @JsonProperty("user_id")
    private Integer userId;
}