package com.harmoniq.backend.auth.controller;

import com.harmoniq.backend.auth.dto.AuthResponseDTO;
import com.harmoniq.backend.auth.dto.LoginRequestDTO;
import com.harmoniq.backend.auth.dto.RegisterRequestDTO;
import com.harmoniq.backend.auth.service.AuthService;
import com.harmoniq.backend.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<String> register(@Valid @RequestBody RegisterRequestDTO request) {
        return ApiResponse.<String>builder()
                .success(true)
                .message("Registration successful")
                .data(authService.register(request))
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ApiResponse.<AuthResponseDTO>builder()
                .success(true)
                .message("Login successful")
                .data(authService.login(request))
                .build();
    }
}