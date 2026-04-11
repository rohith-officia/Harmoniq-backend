package com.harmoniq.backend.user.controller;

import com.harmoniq.backend.common.response.ApiResponse;
import com.harmoniq.backend.user.dto.UserResponse;
import com.harmoniq.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserResponse> getCurrentUser() {
        return ApiResponse.<UserResponse>builder()
                .success(true)
                .message("Current user fetched successfully")
                .data(userService.getCurrentUser())
                .build();
    }
}