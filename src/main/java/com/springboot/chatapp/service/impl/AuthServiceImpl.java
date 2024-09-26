package com.springboot.chatapp.service.impl;

import com.springboot.chatapp.domain.entity.Role;
import com.springboot.chatapp.domain.entity.User;
import com.springboot.chatapp.exception.ChatAppAPIException;
import com.springboot.chatapp.payload.secutiry.*;
import com.springboot.chatapp.security.JwtTokenProvider;
import com.springboot.chatapp.service.AuthService;
import com.springboot.chatapp.service.RoleService;
import com.springboot.chatapp.service.UserService;
import com.springboot.chatapp.utils.UserUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;


    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserService userService,
                           RoleService roleService,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDto.getUsernameOrEmail(), loginRequestDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        User user = userService.findByUsername(((UserDetails) authentication.getPrincipal()).getUsername());

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(token, UserUtils.mapToUserProfile(user));
        return loginResponseDTO;
    }

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDto) {

        if(userService.existsByUsername(registerRequestDto.getUsername())){
            throw new ChatAppAPIException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
        }

        if(userService.existsByEmail(registerRequestDto.getEmail())){
            throw new ChatAppAPIException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
        }

        User user = new User();
        user.setFullName(registerRequestDto.getFullName());
        user.setUsername(registerRequestDto.getUsername());
        user.setEmail(registerRequestDto.getEmail());

        user.setPasswordHash(passwordEncoder.encode(registerRequestDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleService.findByName("ROLE_USER");
        roles.add(userRole);
        user.setRoles(roles);

        User savedUser = userService.save(user);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                savedUser.getUsername(), registerRequestDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        RegisterResponseDTO registerResponseDTO = new RegisterResponseDTO(token, UserUtils.mapToUserProfile(savedUser));

        return registerResponseDTO;
    }
}
