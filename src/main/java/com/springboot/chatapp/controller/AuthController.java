package com.springboot.chatapp.controller;

import com.springboot.chatapp.model.dto.login.LoginRequestDto;
import com.springboot.chatapp.model.dto.login.LoginResponseDto;
import com.springboot.chatapp.model.dto.register.RegisterRequestDto;
import com.springboot.chatapp.model.dto.register.RegisterResponseDto;
import com.springboot.chatapp.repository.UserRepository;
import com.springboot.chatapp.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService,
                          UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping(value = {"/login", "/signIn"})
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        LoginResponseDto loginResponseDTO = authService.login(loginRequestDto);
        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping(value = {"/register", "/signUp"})
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequestDto){
        RegisterResponseDto registerResponseDTO =  authService.register(registerRequestDto);
        return new ResponseEntity<>(registerResponseDTO, HttpStatus.CREATED);
    }
}