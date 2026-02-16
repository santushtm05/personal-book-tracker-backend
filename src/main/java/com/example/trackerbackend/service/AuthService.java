package com.example.trackerbackend.service;

import com.example.trackerbackend.DTO.request.auth.LoginRequest;
import com.example.trackerbackend.DTO.response.auth.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
    void logout(String token);
}