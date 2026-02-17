package com.example.trackerbackend.controller;

import com.example.trackerbackend.DTO.request.user.UserUpdationRequestDTO;
import com.example.trackerbackend.DTO.response.APIResponse;
import com.example.trackerbackend.DTO.response.UserDTO;
import com.example.trackerbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/update/{id}")
    public ResponseEntity<APIResponse<UserDTO>> updateUser(@Valid @RequestBody UserUpdationRequestDTO userUpdationRequestDTO, @PathVariable Integer id) {
        UserDTO updatedUser = this.userService.updateUser(userUpdationRequestDTO, id);
        APIResponse<UserDTO> response = APIResponse.<UserDTO>builder()
                .success(true)
                .error(null)
                .message("User Updated Successfully!")
                .data(updatedUser)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse<String>> deleteUser(@PathVariable Integer id) {
        this.userService.softDelete(id);
        APIResponse<String> response = APIResponse.<String>builder()
                .success(true)
                .error(null)
                .message("User Deleted!")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}