package com.springboot.chatapp.controller;

import com.springboot.chatapp.payload.secutiry.JWTAuthResponse;
import com.springboot.chatapp.payload.secutiry.LoginDTO;
import com.springboot.chatapp.payload.secutiry.RegisterDTO;
import com.springboot.chatapp.service.AuthService;
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

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = {"/login", "/signIn"})
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDTO loginDto){
        String token = authService.login(loginDto);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping(value = {"/register", "/signUp"})
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDto){
        String response = authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}