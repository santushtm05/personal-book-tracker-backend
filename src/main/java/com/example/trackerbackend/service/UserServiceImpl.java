package com.example.trackerbackend.service;

import com.example.trackerbackend.DAO.UserDAO;
import com.example.trackerbackend.DTO.request.user.UserCreationRequestDTO;
import com.example.trackerbackend.DTO.request.user.UserUpdationRequestDTO;
import com.example.trackerbackend.DTO.response.UserDTO;
import com.example.trackerbackend.entity.User;
import com.example.trackerbackend.utils.conversion.EntityConversionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(UserCreationRequestDTO request) {

        // Check duplicate username
        if (userDAO.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        user = userDAO.save(user);

        return EntityConversionUtils.toUserDTO(user);
    }

    @Override
    public UserDTO updateUser(UserUpdationRequestDTO request) {

        User user = userDAO.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getDeletedAt() != null) {
            throw new RuntimeException("User is deleted");
        }

        // Null-safe patch update
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

        user.setUpdatedAt(LocalDateTime.now());

        user = userDAO.save(user);

        return EntityConversionUtils.toUserDTO(user);
    }

    @Override
    public void softDelete(Integer userId) {

        User user = userDAO.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getDeletedAt() != null) {
            throw new RuntimeException("User already deleted");
        }

        user.setDeletedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userDAO.save(user);
    }

    @Override
    public UserDTO getUserById(Integer userId) {
        User userDB = userDAO.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return EntityConversionUtils.toUserDTO(userDB);
    }

    @Override
    public User getById(Integer userId) {
        User userDB = userDAO.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userDB;
    }

    @Override
    public User getByUsername(String username) {
        User userDB = userDAO.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return userDB;
    }
}