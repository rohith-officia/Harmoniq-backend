package com.harmoniq.backend.auth.service.serviceImp;

import com.harmoniq.backend.auth.dto.AuthResponseDTO;
import com.harmoniq.backend.auth.dto.LoginRequestDTO;
import com.harmoniq.backend.auth.dto.RegisterRequestDTO;
import com.harmoniq.backend.auth.security.JwtService;
import com.harmoniq.backend.auth.service.AuthService;
import com.harmoniq.backend.auth.service.serviceImp.UserCacheService;
import com.harmoniq.backend.common.exception.UserAlreadyExistsException;
import com.harmoniq.backend.user.entity.Role;
import com.harmoniq.backend.user.entity.User;
import com.harmoniq.backend.user.entity.UserStatus;
import com.harmoniq.backend.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserCacheService userCacheService;

    // =========================
    // REGISTER
    // =========================
    @Override
    @CacheEvict(value = "users", allEntries = true)
    public String register(RegisterRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : Role.USER)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);

        return "User registered successfully";
    }

    // =========================
    // LOGIN
    // =========================
    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // ✅ CACHE HAPPENS HERE (IMPORTANT FIX)
        User user = userCacheService.getUserByEmail(request.getEmail());

        String token = jwtService.generateToken(user.getEmail());

        return AuthResponseDTO.builder()
                .token(token)
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}