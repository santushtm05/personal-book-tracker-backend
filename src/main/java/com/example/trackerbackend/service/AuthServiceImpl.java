package com.example.trackerbackend.service;

import com.example.trackerbackend.DAO.TokenDAO;
import com.example.trackerbackend.DTO.request.auth.LoginRequest;
import com.example.trackerbackend.DTO.response.auth.LoginResponse;
import com.example.trackerbackend.entity.BlacklistedToken;
import com.example.trackerbackend.utils.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final TokenDAO tokenDAO;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public void logout(String token) {
        if(tokenDAO.existsByToken(token)){
            return;
        }
        LocalDateTime expiry = jwtUtil.extractExpiration(token);
        BlacklistedToken blacklistedToken = BlacklistedToken.builder()
                .token(token)
                .expiryDate(expiry)
                .build();
        tokenDAO.save(blacklistedToken);
    }
}