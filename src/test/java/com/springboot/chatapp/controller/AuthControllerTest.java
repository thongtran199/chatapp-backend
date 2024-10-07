package com.springboot.chatapp.controller;

import com.springboot.chatapp.AbstractTestcontainersTest;
import com.springboot.chatapp.model.dto.login.LoginRequestDto;
import com.springboot.chatapp.model.dto.login.LoginResponseDto;
import com.springboot.chatapp.model.dto.register.RegisterRequestDto;
import com.springboot.chatapp.model.dto.register.RegisterResponseDto;
import com.springboot.chatapp.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest extends AbstractTestcontainersTest {

    public static final String API_AUTH_PATH = "/api/auth";

    @Autowired
    TestRestTemplate testRestTemplate;
    private String registeredEmail1 = "test1" + UUID.randomUUID() + "@gmail.com";
    private String registeredUsername1 = "username1" + UUID.randomUUID();

    private String registeredPassword = "Matkhaunayratmanh123@";

    @BeforeEach
    void setUp() {
        String fullName = "Tran Van Thong";
        RegisterRequestDto registerRequest = new RegisterRequestDto(fullName, registeredUsername1, registeredEmail1, registeredPassword);

        ResponseEntity<RegisterResponseDto> registerResponse = testRestTemplate.postForEntity(
                API_AUTH_PATH + "/register",
                registerRequest,
                RegisterResponseDto.class
        );

        if (registerResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
            System.out.println("Response Body: " + registerResponse.getBody());
        }

        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(registerResponse.getBody()).isNotNull();
        assertThat(registerResponse.getBody().getUser().getUserId()).isNotNull();
    }

    @Test
    void login() {
        LoginRequestDto loginRequest = new LoginRequestDto(registeredEmail1, registeredPassword);

        ResponseEntity<LoginResponseDto> loginResponse = testRestTemplate.postForEntity(
                API_AUTH_PATH + "/login",
                loginRequest,
                LoginResponseDto.class
        );

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).isNotNull();
        assertThat(loginResponse.getBody().getAccessToken()).isNotBlank();
    }

    public static String getAccessToken(TestRestTemplate testRestTemplate, String registeredEmail, String registeredPassword) {
        LoginRequestDto loginRequest = new LoginRequestDto(registeredEmail, registeredPassword);
        ResponseEntity<LoginResponseDto> loginResponse = testRestTemplate.postForEntity(
                API_AUTH_PATH + "/login",
                loginRequest,
                LoginResponseDto.class
        );

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        return loginResponse.getBody().getAccessToken();
    }

    public static RegisterResponseDto registerUser(TestRestTemplate restTemplate, String username, String email, String fullName, String password) {
        RegisterRequestDto registerRequest = new RegisterRequestDto(fullName, username, email, password);
        ResponseEntity<RegisterResponseDto> registerResponse = restTemplate.postForEntity(
                API_AUTH_PATH + "/register",
                registerRequest,
                RegisterResponseDto.class
        );

        if (registerResponse.getStatusCode().is2xxSuccessful() && registerResponse.getBody() != null) {
            return registerResponse.getBody();
        }
        throw new RuntimeException("Failed to register user");
    }

}
