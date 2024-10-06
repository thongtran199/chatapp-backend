package com.springboot.chatapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.chatapp.model.dto.friendship.FriendshipRequestDto;
import com.springboot.chatapp.model.dto.user.SearchedUserResponseDto;
import com.springboot.chatapp.model.entity.Friendship;
import com.springboot.chatapp.model.exception.ChatAppAPIException;
import com.springboot.chatapp.service.manager.FriendshipManager;
import com.springboot.chatapp.utils.mapper.impl.FriendshipMapper;
import com.springboot.chatapp.utils.mapper.impl.UserMapper;
import com.springboot.chatapp.service.FriendshipService;
import com.springboot.chatapp.utils.UserUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/friendship")
public class FriendshipController {
    private final FriendshipService friendshipService;
    private final FriendshipManager friendshipManager;
    private final UserMapper userMapper;
    private final FriendshipMapper friendshipMapper;

    public FriendshipController(
            FriendshipService friendshipService,
            FriendshipManager friendshipManager,
            UserMapper userMapper,
            FriendshipMapper friendshipMapper) {

        this.friendshipService = friendshipService;
        this.friendshipManager = friendshipManager;
        this.userMapper = userMapper;
        this.friendshipMapper = friendshipMapper;
    }

    @PostMapping("/send-friend-request")
    public ResponseEntity<Void> sendFriendRequest(@RequestBody FriendshipRequestDto friendshipRequestDTO) {
        try {
            friendshipManager.sendFriendRequestAndNotification(friendshipRequestDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (JsonProcessingException e) {
            throw new ChatAppAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't process JSON of NotificationSocketDTO");
        }
    }

    @PostMapping("/accept/{friendshipId}")
    public ResponseEntity<Void> acceptFriendRequest(@PathVariable Long friendshipId) {
        try {
            friendshipManager.acceptFriendRequestAndNotification(friendshipId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (JsonProcessingException e) {
            throw new ChatAppAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't process JSON of NotificationSocketDTO");
        }
    }

    @PostMapping("/unfriend/{userId1}/{userId2}")
    public ResponseEntity<Void> unFriend(
            @PathVariable Long userId1, @PathVariable Long userId2) {
        friendshipService.unFriend(userId1, userId2);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/decline/{friendshipId}")
    public ResponseEntity<Void> declineFriendRequest(
            @PathVariable Long friendshipId) {
        friendshipService.declineFriendRequest(friendshipId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/revoke/{friendshipId}")
    public ResponseEntity<Void> revokeFriendRequest(
            @PathVariable Long friendshipId) {
        friendshipService.revokeFriendRequest(friendshipId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/friends/{userId}")
    public ResponseEntity<List<SearchedUserResponseDto>> getAcceptedFriendshipsByUserId(@PathVariable Long userId) {
        List<SearchedUserResponseDto> searchedUserResponseDtos = friendshipManager.findAcceptedFriendshipsByUserId(userId);
        return ResponseEntity.ok(searchedUserResponseDtos);
    }

    @GetMapping("/sent/{userId}")
    public ResponseEntity<List<SearchedUserResponseDto>> getAllSentFriendRequests(@PathVariable Long userId) {
        List<Friendship> friendships = friendshipService.findPendingFriendshipsByRequesterId(userId);
        List<SearchedUserResponseDto> searchedUserResponseDtos = friendships.stream()
                .map(friendship -> {
                    Optional<Friendship> friendshipOptional = Optional.of(friendship);
                    return UserUtils.getSearchedUserResponseDTOByUserAndFriendship(friendship.getRequestedUser(), friendshipOptional);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(searchedUserResponseDtos);
    }

    @GetMapping("/received/{userId}")
    public ResponseEntity<List<SearchedUserResponseDto>> getAllReceivedPendingFriendRequests(@PathVariable Long userId) {
        List<Friendship> friendships = friendshipService.findAllReceivedPendingFriendRequests(userId);
        List<SearchedUserResponseDto> searchedUserResponseDtos = friendships.stream()
                .map(friendship -> {
                    Optional<Friendship> friendshipOptional = Optional.of(friendship);
                    return UserUtils.getSearchedUserResponseDTOByUserAndFriendship(friendship.getRequestedUser(), friendshipOptional);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(searchedUserResponseDtos);
    }



}
