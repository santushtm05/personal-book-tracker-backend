package com.example.trackerbackend.service;

import com.example.trackerbackend.DAO.UserDAO;
import com.example.trackerbackend.DTO.request.user.UserCreationRequestDTO;
import com.example.trackerbackend.DTO.request.user.UserUpdationRequestDTO;
import com.example.trackerbackend.DTO.response.UserDTO;
import com.example.trackerbackend.entity.User;
import com.example.trackerbackend.entity.principal.CustomUserDetails;
import com.example.trackerbackend.exception.DuplicateResourceException;
import com.example.trackerbackend.exception.ForbiddenException;
import com.example.trackerbackend.exception.ResourceNotFoundException;
import com.example.trackerbackend.utils.conversion.EntityConversionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    private boolean validateRequestedUserId(Integer requestUserId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails =  (CustomUserDetails) authentication.getPrincipal();
        Integer userId = userDetails.getId();
        if (requestUserId.equals(userId)) {
            return false;
        }
        return true;
    }

    @Override
    public UserDTO createUser(UserCreationRequestDTO request) {

        // Check duplicate username
        System.out.println("Username In request: "+request.getUsername());
        System.out.println("Executing CHeck: "+userDAO.existsByUsernameAndDeletedAtIsNull(request.getUsername()));
        if (userDAO.existsByUsernameAndDeletedAtIsNull(request.getUsername())) {
            throw new DuplicateResourceException("Username");
        }

        User user = User.builder()
                .id(null)
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
        if(!validateRequestedUserId(request.getId())) {
            throw new ForbiddenException("Forbidden!");
        }
        User user = userDAO.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getDeletedAt() != null) {
            throw new ResourceNotFoundException("User");
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
        if(validateRequestedUserId(userId)) {
            throw new ForbiddenException("Forbidden!");
        }
        User user = userDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User"));

        if (user.getDeletedAt() != null) {
            throw new ResourceNotFoundException("User");
        }

        user.setDeletedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userDAO.save(user);
    }

    @Override
    public UserDTO getUserById(Integer userId) {
        User userDB = userDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        return EntityConversionUtils.toUserDTO(userDB);
    }

    @Override
    public User getById(Integer userId) {
        User userDB = userDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        return userDB;
    }

    @Override
    public User getByUsername(String username) {
        User userDB = userDAO.findByUsernameAndDeletedAtIsNull(username).orElseThrow(() -> new ResourceNotFoundException("User"));
        return userDB;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userDAO.existsByUsernameAndDeletedAtIsNull(username);
    }
}