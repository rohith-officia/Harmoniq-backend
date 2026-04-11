package com.harmoniq.backend.auth.service;

import com.harmoniq.backend.auth.dto.AuthResponseDTO;
import com.harmoniq.backend.auth.dto.LoginRequestDTO;
import com.harmoniq.backend.auth.dto.RegisterRequestDTO;
import jakarta.validation.Valid;

public interface AuthService  {

    AuthResponseDTO login(@Valid LoginRequestDTO request);
    String register(@Valid RegisterRequestDTO request);
}
