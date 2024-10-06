package com.springboot.chatapp.service;

import com.springboot.chatapp.model.dto.login.LoginRequestDto;
import com.springboot.chatapp.model.dto.login.LoginResponseDto;
import com.springboot.chatapp.model.dto.register.RegisterRequestDto;
import com.springboot.chatapp.model.dto.register.RegisterResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);

    RegisterResponseDto register(RegisterRequestDto registerRequestDto);
}
