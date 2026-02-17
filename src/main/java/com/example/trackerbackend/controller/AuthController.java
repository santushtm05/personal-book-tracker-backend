package com.example.trackerbackend.controller;

import com.example.trackerbackend.DTO.request.auth.LoginRequest;
import com.example.trackerbackend.DTO.request.user.UserCreationRequestDTO;
import com.example.trackerbackend.DTO.response.APIResponse;
import com.example.trackerbackend.DTO.response.UserDTO;
import com.example.trackerbackend.DTO.response.auth.LoginResponse;
import com.example.trackerbackend.entity.User;
import com.example.trackerbackend.entity.principal.CustomUserDetails;
import com.example.trackerbackend.service.AuthService;
import com.example.trackerbackend.service.UserService;
import com.example.trackerbackend.utils.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<APIResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        User userDB = userService.getByUsername(loginRequest.getUsername());
        String token = jwtUtil.generateToken(userDB.getId());
        System.out.println("Token Generated: " + token);
        LoginResponse loginResponse = LoginResponse.builder()
                .token(token)
                .username(userDB.getUsername())
                .userId(userDB.getId())
                .build();

        APIResponse<LoginResponse> response = APIResponse.<LoginResponse>builder()
                .success(true)
                .message("Login Successful!")
                .data(loginResponse)
                .error(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<APIResponse<String>> register(@RequestBody UserCreationRequestDTO request){
        this.userService.createUser(request);
        APIResponse<String> response = APIResponse.<String>builder()
                .success(true)
                .message("Registration Successful!")
                .data(null)
                .error(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<APIResponse<String>> logout(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        APIResponse<String> response = APIResponse.<String>builder()
                .success(true)
                .data(null)
                .error(null)
                .timestamp(LocalDateTime.now())
                .build();
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setMessage("Invalid Token");
            return ResponseEntity.badRequest().body(response);
        }

        String token = authHeader.substring(7);

        authService.logout(token);
        response.setMessage("Logged Out Successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<APIResponse<UserDTO>> getCurrentUser() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = userDetails.getId();

        UserDTO userDB = userService.getUserById(userId);

        APIResponse<UserDTO> response = APIResponse.<UserDTO>builder()
                .success(true)
                .data(userDB)
                .error(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/username-available")
    public ResponseEntity<APIResponse<Boolean>> isUsernameAvailable(
            @RequestParam String username
    ) {
        boolean available = userService.existsByUsername(username);
        APIResponse<Boolean> response = APIResponse.<Boolean>builder()
                .success(true)
                .error(null)
                .message(available ? "Username Not Available!" : "Username Available!")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}