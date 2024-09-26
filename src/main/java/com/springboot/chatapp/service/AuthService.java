package com.springboot.chatapp.service;

import com.springboot.chatapp.domain.entity.User;
import com.springboot.chatapp.payload.secutiry.*;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO loginRequestDto);

    RegisterResponseDTO register(RegisterRequestDTO registerRequestDto);
}
