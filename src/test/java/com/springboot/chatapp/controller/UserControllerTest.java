package com.springboot.chatapp.controller;

import com.springboot.chatapp.AbstractTestcontainersTest;
import com.springboot.chatapp.TestDataUtils;
import com.springboot.chatapp.model.dto.register.RegisterResponseDto;
import com.springboot.chatapp.model.dto.user.SearchedUserResponseDto;
import com.springboot.chatapp.model.dto.user.UserResponseDto;
import com.springboot.chatapp.model.dto.user.UserProfileResponseDto;
import com.springboot.chatapp.model.entity.User;
import com.springboot.chatapp.repository.UserRepository;
import com.springboot.chatapp.service.UserService;
import com.springboot.chatapp.utils.mapper.impl.UserMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.springboot.chatapp.controller.AuthControllerTest.getAccessToken;
import static com.springboot.chatapp.controller.AuthControllerTest.registerUser;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest extends AbstractTestcontainersTest {

    private static final String API_USER_PATH = "/api/user";

    @Autowired
    private TestRestTemplate testRestTemplate;

    private UserProfileResponseDto testUser;
    private String email = "email" + UUID.randomUUID() + "@gmail.com";
    private String username = "username" + UUID.randomUUID();

    @BeforeEach
    void setUp() {
        testUser = registerUser(testRestTemplate, username, email,"Tran Van Thong").getUser();
    }

    @Test
    void getUserByUsername() {
        ResponseEntity<UserResponseDto> response = testRestTemplate.getForEntity(
                API_USER_PATH + "/username/" + username,
                UserResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo(testUser.getUsername());
    }

    @Test
    void getUserByEmail() {
        ResponseEntity<UserResponseDto> response = testRestTemplate.getForEntity(
                API_USER_PATH + "/email/" + email,
                UserResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo(email);
    }

    @Test
    void getUserProfileById() {
        ResponseEntity<UserProfileResponseDto> response = testRestTemplate.getForEntity(
                API_USER_PATH + "/profile/" + testUser.getUserId(),
                UserProfileResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFullName()).isEqualTo(testUser.getFullName());
    }

    @Test
    void getUserById() {
        ResponseEntity<UserResponseDto> response = testRestTemplate.getForEntity(
                API_USER_PATH + "/" + testUser.getUserId(),
                UserResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUserId()).isEqualTo(testUser.getUserId());
    }

    @Test
    void checkUsernameExists() {
        ResponseEntity<Boolean> response = testRestTemplate.getForEntity(
                API_USER_PATH + "/exists/username/" + testUser.getUsername(),
                Boolean.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isTrue();
    }

    @Test
    void checkEmailExists() {
        ResponseEntity<Boolean> response = testRestTemplate.getForEntity(
                API_USER_PATH + "/exists/email/" + testUser.getEmail(),
                Boolean.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isTrue();
    }

    @Test
    void searchByFullNameContaining() {
        ResponseEntity<List<SearchedUserResponseDto>> response = testRestTemplate.exchange(
                API_USER_PATH + "/search?fullName=" + testUser.getFullName() + "&userId=" + testUser.getUserId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SearchedUserResponseDto>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    void getMeByJwt() {
        String accessToken = getAccessToken(testRestTemplate, email );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<UserResponseDto> response = testRestTemplate.exchange(
                API_USER_PATH + "/get-me",
                HttpMethod.GET,
                entity,
                UserResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

}
