package com.springboot.chatapp.controller;

import com.springboot.chatapp.domain.dto.user.response.UserProfileDTO;
import com.springboot.chatapp.domain.entity.User;
import com.springboot.chatapp.payload.secutiry.*;
import com.springboot.chatapp.repository.UserRepository;
import com.springboot.chatapp.service.AuthService;
import com.springboot.chatapp.utils.UserUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService,
                          UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping(value = {"/login", "/signIn"})
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDto){
        LoginResponseDTO loginResponseDTO = authService.login(loginRequestDto);
        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping(value = {"/register", "/signUp"})
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDto){
        RegisterResponseDTO registerResponseDTO =  authService.register(registerRequestDto);
        return new ResponseEntity<>(registerResponseDTO, HttpStatus.CREATED);
    }
}