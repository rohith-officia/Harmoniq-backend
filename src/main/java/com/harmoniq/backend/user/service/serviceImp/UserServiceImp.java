package com.harmoniq.backend.user.service.serviceImp;

import com.harmoniq.backend.common.exception.ResourceNotFoundException;
import com.harmoniq.backend.user.dto.UserResponse;
import com.harmoniq.backend.user.entity.User;
import com.harmoniq.backend.user.repository.UserRepository;
import com.harmoniq.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}