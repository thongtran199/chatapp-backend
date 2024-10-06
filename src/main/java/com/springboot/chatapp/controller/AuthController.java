package com.springboot.chatapp.controller;

import com.springboot.chatapp.model.dto.login.LoginRequestDto;
import com.springboot.chatapp.model.dto.login.LoginResponseDto;
import com.springboot.chatapp.model.dto.register.RegisterRequestDto;
import com.springboot.chatapp.model.dto.register.RegisterResponseDto;
import com.springboot.chatapp.repository.UserRepository;
import com.springboot.chatapp.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication Management", description = "Endpoints for user authentication")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @Operation(summary = "User login", description = "Logs in a user with their credentials",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User logged in successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials")
            })
    @PostMapping(value = {"/login", "/signIn"})
    public ResponseEntity<LoginResponseDto> login(
            @Parameter(description = "Login Request DTO", required = true) @Valid @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDTO = authService.login(loginRequestDto);
        return ResponseEntity.ok(loginResponseDTO);
    }

    @Operation(summary = "User registration", description = "Registers a new user",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User registered successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data")
            })
    @PostMapping(value = {"/register", "/signUp"})
    public ResponseEntity<RegisterResponseDto> register(
            @Parameter(description = "Register Request DTO", required = true) @Valid @RequestBody RegisterRequestDto registerRequestDto) {
        RegisterResponseDto registerResponseDTO = authService.register(registerRequestDto);
        return new ResponseEntity<>(registerResponseDTO, HttpStatus.CREATED);
    }
}
