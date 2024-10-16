package com.springboot.chatapp.controller;

import com.springboot.chatapp.model.dto.notification.NotificationRequestDto;
import com.springboot.chatapp.model.dto.notification.NotificationResponseDto;
import com.springboot.chatapp.model.dto.user.UserProfileResponseDto;
import com.springboot.chatapp.model.entity.Notification;
import com.springboot.chatapp.model.enums.NotificationType;
import com.springboot.chatapp.repository.UserRepository;
import com.springboot.chatapp.service.NotificationService;
import com.springboot.chatapp.utils.mapper.impl.NotificationMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;
import java.util.UUID;

import static com.springboot.chatapp.controller.AuthControllerTest.getAccessToken;
import static com.springboot.chatapp.controller.AuthControllerTest.registerUser;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NotificationControllerTest {

    private static final String API_NOTIFICATION_PATH = "/api/notification";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private NotificationService notificationService;
    private UserProfileResponseDto user;
    private String registeredEmail =  "notiemail" + UUID.randomUUID() + "@gmail.com";
    private String registeredUsername =  "notiusername" + UUID.randomUUID() + "@gmail.com";

    @BeforeEach
    void setUp() {
        user = registerUser(testRestTemplate, registeredUsername, registeredEmail, "Sender User 1").getUser();
    }

    @Test
    void getNotificationsByUserId() {
        ResponseEntity<List<NotificationResponseDto>> response = testRestTemplate.exchange(
                API_NOTIFICATION_PATH + "/user/{userId}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<NotificationResponseDto>>() {},
                user.getUserId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void markNotificationAsRead() {
        NotificationRequestDto notificationRequestDto = new NotificationRequestDto();
        notificationRequestDto.setUserId(user.getUserId());
        notificationRequestDto.setType(NotificationType.MESSAGE);
        notificationRequestDto.setReferenceId(1L);

        Notification notification =  notificationService.save(notificationRequestDto);

        String accessToken = getAccessToken(testRestTemplate, registeredEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = testRestTemplate.postForEntity(
                API_NOTIFICATION_PATH + "/mark-read/{notificationId}",
                entity,
                Void.class,
                notification.getNotificationId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteNotification() {

        NotificationRequestDto notificationRequestDto = new NotificationRequestDto();
        notificationRequestDto.setUserId(user.getUserId());
        notificationRequestDto.setType(NotificationType.MESSAGE);
        notificationRequestDto.setReferenceId(1L);

        Notification notification =  notificationService.save(notificationRequestDto);

        String accessToken = getAccessToken(testRestTemplate, registeredEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);


        ResponseEntity<Void> response = testRestTemplate.exchange(
                API_NOTIFICATION_PATH + "/{notificationId}",
                HttpMethod.DELETE,
                entity,
                Void.class,
                notification.getNotificationId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
