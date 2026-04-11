package com.harmoniq.backend.user.service;

import com.harmoniq.backend.user.dto.UserResponse;

public interface UserService {
    UserResponse getCurrentUser();
}