package com.springboot.chatapp.service;

import com.springboot.chatapp.TestDataUtils;
import com.springboot.chatapp.model.dto.login.LoginRequestDto;
import com.springboot.chatapp.model.dto.login.LoginResponseDto;
import com.springboot.chatapp.model.dto.register.RegisterRequestDto;
import com.springboot.chatapp.model.dto.register.RegisterResponseDto;
import com.springboot.chatapp.model.entity.Role;
import com.springboot.chatapp.model.entity.User;
import com.springboot.chatapp.model.exception.ChatAppAPIException;
import com.springboot.chatapp.security.JwtTokenProvider;
import com.springboot.chatapp.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    @Captor
    ArgumentCaptor<User> userArgumentCaptor;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(authenticationManager, userService, roleService, passwordEncoder, jwtTokenProvider);
    }

    @Test
    void login() {
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsernameOrEmail("thongtran");
        loginRequest.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        org.springframework.security.core.userdetails.UserDetails userDetails = mock(org.springframework.security.core.userdetails.UserDetails.class);
        when(userDetails.getUsername()).thenReturn("thongtran");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn("token");

        User user = TestDataUtils.createUser("thongtran", "thongtran@gmail.com", "Tran Van Thong");
        when(userService.findByUsername("thongtran")).thenReturn(user);

        LoginResponseDto loginResponse = authService.login(loginRequest);

        assertThat(loginResponse).isNotNull();
        assertThat(loginResponse.getAccessToken()).isEqualTo("token");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateToken(any(Authentication.class));
        verify(userService).findByUsername("thongtran");
    }


    @Test
    void register() {
        RegisterRequestDto registerRequest = new RegisterRequestDto();
        registerRequest.setUsername("newUser");
        registerRequest.setEmail("newUser@example.com");
        registerRequest.setPassword("password");
        registerRequest.setFullName("New User");

        when(userService.existsByUsername(anyString())).thenReturn(false);
        when(userService.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        Role role = new Role();
        role.setName("ROLE_USER");
        when(roleService.findByName(anyString())).thenReturn(role);

        User user = new User();
        user.setUsername("newUser");
        when(userService.save(any(User.class))).thenReturn(user);

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn("token");

        RegisterResponseDto registerResponse = authService.register(registerRequest);

        assertThat(registerResponse).isNotNull();
        assertThat(registerResponse.getAccessToken()).isEqualTo("token");
        verify(userService).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser.getUsername()).isEqualTo("newUser");
        assertThat(capturedUser.getPasswordHash()).isEqualTo("encodedPassword");
        assertThat(capturedUser.getRoles()).contains(role);
    }

    @Test
    void register_UsernameExists() {
        RegisterRequestDto registerRequest = new RegisterRequestDto();
        registerRequest.setUsername("existingUser");

        when(userService.existsByUsername(anyString())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(ChatAppAPIException.class)
                .hasMessageContaining("Username is already exists!")
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    void register_EmailExists() {
        RegisterRequestDto registerRequest = new RegisterRequestDto();
        registerRequest.setEmail("existingEmail@example.com");

        when(userService.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(ChatAppAPIException.class)
                .hasMessageContaining("Email is already exists!")
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }
}
