package com.springboot.chatapp.controller;

import com.springboot.chatapp.TestDataUtils;
import com.springboot.chatapp.model.dto.friendship.FriendshipRequestDto;
import com.springboot.chatapp.model.dto.friendship.FriendshipResponseDto;
import com.springboot.chatapp.model.dto.user.SearchedUserResponseDto;
import com.springboot.chatapp.model.dto.user.UserProfileResponseDto;
import com.springboot.chatapp.model.entity.Friendship;
import com.springboot.chatapp.model.entity.User;
import com.springboot.chatapp.service.FriendshipService;
import com.springboot.chatapp.service.UserService;
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
class FriendshipControllerTest {

    private static final String API_FRIENDSHIP_PATH = "/api/friendship";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendshipService friendshipService;

    private UserProfileResponseDto senderUser;
    private UserProfileResponseDto receiverUser;
    private String senderUsername = "sender" + UUID.randomUUID();
    private String receiverUsername = "receiver" + UUID.randomUUID();
    private String senderEmail = "sender" + UUID.randomUUID() + "@gmail.com";
    private String receiverEmail = "receiver" + UUID.randomUUID() + "@gmail.com";
    private String registeredPassword = "Matkhaunayratmanh123@";

    @BeforeEach
    void setUp() {
        senderUser = registerUser(testRestTemplate, senderUsername, senderEmail, "Sender User", registeredPassword).getUser();
        receiverUser = registerUser(testRestTemplate, receiverUsername, receiverEmail, "Receiver User", registeredPassword).getUser();
    }

    @Test
    void acceptFriendRequest() {
        FriendshipRequestDto friendshipRequestDto = new FriendshipRequestDto(receiverUser.getUserId(), senderUser.getUserId());
        Friendship friendship = friendshipService.sendFriendRequest(friendshipRequestDto); // Create friendship here

        String accessToken = getAccessToken(testRestTemplate, senderEmail, registeredPassword);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = testRestTemplate.postForEntity(
                API_FRIENDSHIP_PATH + "/accept/" + friendship.getFriendshipId(),
                entity, // pass entity here instead of null
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void declineFriendRequest() {
        FriendshipRequestDto friendshipRequestDto = new FriendshipRequestDto(receiverUser.getUserId(), senderUser.getUserId());
        Friendship friendship = friendshipService.sendFriendRequest(friendshipRequestDto); // Create friendship here

        String accessToken = getAccessToken(testRestTemplate, senderEmail, registeredPassword);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> declineResponse = testRestTemplate.postForEntity(
                API_FRIENDSHIP_PATH + "/decline/" + friendship.getFriendshipId(),
                entity, // pass entity here instead of null
                Void.class
        );

        assertThat(declineResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void revokeFriendRequest() {
        FriendshipRequestDto friendshipRequestDto = new FriendshipRequestDto(senderUser.getUserId(), receiverUser.getUserId());
        Friendship friendship = friendshipService.sendFriendRequest(friendshipRequestDto); // Create friendship here

        String accessToken = getAccessToken(testRestTemplate, senderEmail, registeredPassword);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> revokeResponse = testRestTemplate.postForEntity(
                API_FRIENDSHIP_PATH + "/revoke/" + friendship.getFriendshipId(),
                entity, // pass entity here instead of null
                Void.class
        );

        assertThat(revokeResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void unFriend() {
        FriendshipRequestDto friendshipRequestDto = new FriendshipRequestDto(senderUser.getUserId(), receiverUser.getUserId());
        Friendship friendship = friendshipService.sendFriendRequest(friendshipRequestDto); // Create friendship here
        friendshipService.acceptFriendRequest(friendship.getFriendshipId()); // Ensure the friendship is accepted

        String accessToken = getAccessToken(testRestTemplate, senderEmail, registeredPassword);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> unfriendResponse = testRestTemplate.postForEntity(
                API_FRIENDSHIP_PATH + "/unfriend/" + senderUser.getUserId() + "/" + receiverUser.getUserId(),
                entity,
                Void.class
        );

        assertThat(unfriendResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    void getAllSentFriendRequests() {
        FriendshipRequestDto friendshipRequestDto = new FriendshipRequestDto(receiverUser.getUserId(), senderUser.getUserId());
        friendshipService.sendFriendRequest(friendshipRequestDto); // Create friendship here

        ResponseEntity<List<SearchedUserResponseDto>> response = testRestTemplate.exchange(
                API_FRIENDSHIP_PATH + "/sent/" + senderUser.getUserId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SearchedUserResponseDto>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getAllReceivedPendingFriendRequests() {
        FriendshipRequestDto friendshipRequestDto = new FriendshipRequestDto(receiverUser.getUserId(), senderUser.getUserId());
        friendshipService.sendFriendRequest(friendshipRequestDto); // Create friendship here

        ResponseEntity<List<SearchedUserResponseDto>> response = testRestTemplate.exchange(
                API_FRIENDSHIP_PATH + "/received/" + receiverUser.getUserId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SearchedUserResponseDto>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
