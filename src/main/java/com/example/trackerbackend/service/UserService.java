package com.example.trackerbackend.service;

import com.example.trackerbackend.DTO.request.user.UserCreationRequestDTO;
import com.example.trackerbackend.DTO.request.user.UserUpdationRequestDTO;
import com.example.trackerbackend.DTO.response.UserDTO;
import com.example.trackerbackend.entity.User;

public interface UserService {
    UserDTO createUser(UserCreationRequestDTO userCreationRequestDTO);
    UserDTO updateUser(UserUpdationRequestDTO userUpdationRequestDTO, Integer userId);
    void softDelete(Integer userId);
    UserDTO getUserById(Integer userId);
    User getByUsername(String username);
    User getById(Integer userId);
    boolean existsByUsername(String username);
}