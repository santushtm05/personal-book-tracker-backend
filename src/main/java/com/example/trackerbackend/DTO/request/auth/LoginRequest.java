package com.example.trackerbackend.DTO.request.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
}