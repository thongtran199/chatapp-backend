package com.springboot.chatapp.service;

import com.springboot.chatapp.payload.secutiry.LoginDTO;
import com.springboot.chatapp.payload.secutiry.RegisterDTO;

public interface AuthService {
    String login(LoginDTO loginDto);

    String register(RegisterDTO registerDto);
}
