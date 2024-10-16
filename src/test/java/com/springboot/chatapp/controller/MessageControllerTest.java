package com.springboot.chatapp.controller;

import com.springboot.chatapp.model.dto.message.MessageRequestDto;
import com.springboot.chatapp.model.dto.message.MessageResponseDto;
import com.springboot.chatapp.model.dto.message.MessageHistoryResponseDto;
import com.springboot.chatapp.model.dto.user.UserProfileResponseDto;
import com.springboot.chatapp.model.entity.Message;
import com.springboot.chatapp.model.entity.User;
import com.springboot.chatapp.repository.UserRepository;
import com.springboot.chatapp.service.MessageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import static com.springboot.chatapp.controller.AuthControllerTest.getAccessToken;
import static com.springboot.chatapp.controller.AuthControllerTest.registerUser;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MessageControllerTest {

    private static final String API_MESSAGE_PATH = "/api/message";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MessageService messageService;



    private UserProfileResponseDto senderUser;
    private UserProfileResponseDto receiverUser;
    private String senderUsername = "senderMessage" + UUID.randomUUID();
    private String receiverUsername = "receiverMessage" + UUID.randomUUID();
    private String senderEmail = "senderMessage" + UUID.randomUUID() + "@gmail.com";
    private String receiverEmail = "receiverMessage" + UUID.randomUUID() + "@gmail.com";

    @BeforeEach
    void setUp() {
        senderUser = registerUser(testRestTemplate, senderUsername, senderEmail, "Sender User").getUser();
        receiverUser = registerUser(testRestTemplate, receiverUsername, receiverEmail, "Receiver User").getUser();
    }

    @Test
    void getConversation() {
        Long userId1 = senderUser.getUserId();
        Long userId2 = receiverUser.getUserId();

        ResponseEntity<List<MessageResponseDto>> response = testRestTemplate.exchange(
                API_MESSAGE_PATH + "/conversation/" + userId1 + "/" + userId2,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MessageResponseDto>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getMessageHistory() {
        Long userId = senderUser.getUserId();

        ResponseEntity<List<MessageHistoryResponseDto>> response = testRestTemplate.exchange(
                API_MESSAGE_PATH + "/chat-history/" + userId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MessageHistoryResponseDto>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void markMessageAsRead() {
        MessageRequestDto messageRequestDto = new MessageRequestDto();
        messageRequestDto.setMessageSenderId(receiverUser.getUserId());
        messageRequestDto.setMessageReceiverId(senderUser.getUserId());
        messageRequestDto.setContent("Hello day la chatapp");

        Message message = messageService.save(messageRequestDto);


        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken(testRestTemplate, senderEmail));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = testRestTemplate.postForEntity(
                API_MESSAGE_PATH + "/mark-read/" + message.getMessageId(),
                entity,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
