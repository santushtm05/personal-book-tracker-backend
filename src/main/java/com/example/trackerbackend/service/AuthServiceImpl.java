package com.example.trackerbackend.service;

import com.example.trackerbackend.DAO.TokenDAO;
import com.example.trackerbackend.DTO.request.auth.LoginRequest;
import com.example.trackerbackend.DTO.response.auth.LoginResponse;
import com.example.trackerbackend.entity.BlacklistedToken;
import com.example.trackerbackend.DAO.UserDAO;
import com.example.trackerbackend.entity.User;
import com.example.trackerbackend.exception.ResourceNotFoundException;
import com.example.trackerbackend.utils.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final TokenDAO tokenDAO;
    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;
    private final UserDAO userDAO;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        User userDB = userDAO.findByUsernameAndDeletedAtIsNull(loginRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User"));

        String token = jwtUtil.generateToken(userDB.getId());

        return LoginResponse.builder()
                .token(token)
                .username(userDB.getUsername())
                .userId(userDB.getId())
                .build();
    }

    @Override
    public void logout(String token) {
        if (tokenDAO.existsByToken(token)) {
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