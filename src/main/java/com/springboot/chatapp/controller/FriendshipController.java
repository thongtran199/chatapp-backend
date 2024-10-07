package com.springboot.chatapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.chatapp.model.dto.friendship.FriendshipRequestDto;
import com.springboot.chatapp.model.dto.friendship.FriendshipResponseDto;
import com.springboot.chatapp.model.dto.user.SearchedUserResponseDto;
import com.springboot.chatapp.model.entity.Friendship;
import com.springboot.chatapp.model.exception.ChatAppAPIException;
import com.springboot.chatapp.service.manager.FriendshipManager;
import com.springboot.chatapp.utils.mapper.impl.FriendshipMapper;
import com.springboot.chatapp.utils.mapper.impl.UserMapper;
import com.springboot.chatapp.service.FriendshipService;
import com.springboot.chatapp.utils.UserUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Friendship Management", description = "Endpoints for friendship")
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

    @Operation(summary = "Send a friend request", description = "Sends a new friend request")
    @PostMapping("/send-friend-request")
    public ResponseEntity<FriendshipResponseDto> sendFriendRequest(
            @Parameter(description = "Friendship Request DTO", required = true) @Valid @RequestBody FriendshipRequestDto friendshipRequestDTO) {
        try {
            Friendship friendship = friendshipManager.sendFriendRequestAndNotification(friendshipRequestDTO);
            return new ResponseEntity<>(friendshipMapper.mapToResponseDTO(friendship), HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            throw new ChatAppAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't process JSON of NotificationSocketDTO");
        }
    }

    @Operation(summary = "Accept a friend request", description = "Accepts a pending friend request")
    @PostMapping("/accept/{friendshipId}")
    public ResponseEntity<Void> acceptFriendRequest(
            @Parameter(description = "ID of the friendship to accept", required = true) @PathVariable Long friendshipId) {
        try {
            friendshipManager.acceptFriendRequestAndNotification(friendshipId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (JsonProcessingException e) {
            throw new ChatAppAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't process JSON of NotificationSocketDTO");
        }
    }

    @Operation(summary = "Unfriend a user", description = "Unfriends a user by their IDs")
    @PostMapping("/unfriend/{userId1}/{userId2}")
    public ResponseEntity<Void> unFriend(
            @Parameter(description = "ID of the first user", required = true) @PathVariable Long userId1,
            @Parameter(description = "ID of the second user", required = true) @PathVariable Long userId2) {
        friendshipService.unFriend(userId1, userId2);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Decline a friend request", description = "Declines a pending friend request")
    @PostMapping("/decline/{friendshipId}")
    public ResponseEntity<Void> declineFriendRequest(
            @Parameter(description = "ID of the friendship to decline", required = true) @PathVariable Long friendshipId) {
        friendshipService.declineFriendRequest(friendshipId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Revoke a friend request", description = "Revokes a pending friend request")
    @PostMapping("/revoke/{friendshipId}")
    public ResponseEntity<Void> revokeFriendRequest(
            @Parameter(description = "ID of the friendship to revoke", required = true) @PathVariable Long friendshipId) {
        friendshipService.revokeFriendRequest(friendshipId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Get accepted friends by user ID", description = "Retrieves a list of accepted friendships by user ID")
    @GetMapping("/friends/{userId}")
    public ResponseEntity<List<SearchedUserResponseDto>> getAcceptedFriendshipsByUserId(
            @Parameter(description = "ID of the user to retrieve friendships for", required = true) @PathVariable Long userId) {
        List<SearchedUserResponseDto> searchedUserResponseDtos = friendshipManager.findAcceptedFriendshipsByUserId(userId);
        return ResponseEntity.ok(searchedUserResponseDtos);
    }

    @Operation(summary = "Get sent friend requests", description = "Retrieves all friend requests sent by a user")
    @GetMapping("/sent/{userId}")
    public ResponseEntity<List<SearchedUserResponseDto>> getAllSentFriendRequests(
            @Parameter(description = "ID of the user who sent friend requests", required = true) @PathVariable Long userId) {
        List<Friendship> friendships = friendshipService.findPendingFriendshipsByRequesterId(userId);
        List<SearchedUserResponseDto> searchedUserResponseDtos = friendships.stream()
                .map(friendship -> {
                    Optional<Friendship> friendshipOptional = Optional.of(friendship);
                    return UserUtils.getSearchedUserResponseDTOByUserAndFriendship(friendship.getRequestedUser(), friendshipOptional);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(searchedUserResponseDtos);
    }

    @Operation(summary = "Get received pending friend requests", description = "Retrieves all pending friend requests received by a user")
    @GetMapping("/received/{userId}")
    public ResponseEntity<List<SearchedUserResponseDto>> getAllReceivedPendingFriendRequests(
            @Parameter(description = "ID of the user who received friend requests", required = true) @PathVariable Long userId) {
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
