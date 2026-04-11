package com.harmoniq.backend.auth.service.serviceImp;

import com.harmoniq.backend.auth.dto.AuthResponseDTO;
import com.harmoniq.backend.auth.dto.LoginRequestDTO;
import com.harmoniq.backend.auth.dto.RegisterRequestDTO;
import com.harmoniq.backend.auth.security.JwtService;
import com.harmoniq.backend.auth.service.AuthService;
import com.harmoniq.backend.common.exception.ResourceNotFoundException;
import com.harmoniq.backend.common.exception.UserAlreadyExistsException;
import com.harmoniq.backend.user.entity.Role;
import com.harmoniq.backend.user.entity.User;
import com.harmoniq.backend.user.entity.UserStatus;
import com.harmoniq.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    @Override
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

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtService.generateToken(user.getEmail());

        return AuthResponseDTO.builder()
                .token(token)
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}