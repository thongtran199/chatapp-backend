package com.springboot.chatapp.controller;

import com.springboot.chatapp.domain.dto.user.request.FriendshipRequestDTO;
import com.springboot.chatapp.domain.dto.user.response.FoundUserResponseDTO;
import com.springboot.chatapp.domain.dto.user.response.FriendshipFoundUserResponseDTO;
import com.springboot.chatapp.domain.dto.user.response.FriendshipResponseDTO;
import com.springboot.chatapp.domain.dto.user.response.UserResponseDTO;
import com.springboot.chatapp.domain.entity.Friendship;
import com.springboot.chatapp.domain.entity.User;
import com.springboot.chatapp.domain.enumerate.FriendshipStatus;
import com.springboot.chatapp.manager.FriendshipManager;
import com.springboot.chatapp.mapper.impl.FriendshipMapper;
import com.springboot.chatapp.mapper.impl.UserMapper;
import com.springboot.chatapp.service.FriendshipService;
import com.springboot.chatapp.utils.FriendshipUtils;
import com.springboot.chatapp.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/friendship")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private FriendshipManager friendshipManager;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FriendshipMapper friendshipMapper;

    @PostMapping("/send-friend-request")
    public ResponseEntity<Void> sendFriendRequest(@RequestBody FriendshipRequestDTO friendshipRequestDTO) {
        friendshipManager.saveFriendRequestAndNotification(friendshipRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/accept/{friendshipId}")
    public ResponseEntity<Void> acceptFriendRequest(
            @PathVariable Long friendshipId) {
        friendshipManager.acceptFriendRequestAndNotification(friendshipId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unfriend/{userId1}/{userId2}")
    public ResponseEntity<Void> unFriend(
            @PathVariable Long userId1, @PathVariable Long userId2) {
        friendshipService.unFriend(userId1, userId2);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/decline/{friendshipId}")
    public ResponseEntity<Void> declineFriendRequest(
            @PathVariable Long friendshipId) {
        friendshipService.declineFriendRequest(friendshipId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/revoke/{friendshipId}")
    public ResponseEntity<Void> revokeFriendRequest(
            @PathVariable Long friendshipId) {
        friendshipService.revokeFriendRequest(friendshipId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/friends/{userId}")
    public ResponseEntity<List<FoundUserResponseDTO>> getAcceptedFriendshipsByUserId(@PathVariable Long userId) {
        List<FoundUserResponseDTO> foundUserResponseDTOS = friendshipManager.findAcceptedFriendshipsByUserId(userId);
        return ResponseEntity.ok(foundUserResponseDTOS);
    }

    @GetMapping("/sent/{userId}")
    public ResponseEntity<List<FoundUserResponseDTO>> getAllSentFriendRequests(@PathVariable Long userId) {
        List<Friendship> friendships = friendshipService.findPendingFriendshipsByRequesterId(userId);
        List<FoundUserResponseDTO> foundUserResponseDTOS = friendships.stream()
                .map(friendship -> {
                    Optional<Friendship> friendshipOptional = Optional.of(friendship);
                    return UserUtils.getFoundUserResponseDTOByUserAndFriendship(friendship.getRequestedUser(), friendshipOptional);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(foundUserResponseDTOS);
    }

    @GetMapping("/received/{userId}")
    public ResponseEntity<List<FoundUserResponseDTO>> getAllReceivedPendingFriendRequests(@PathVariable Long userId) {
        List<Friendship> friendships = friendshipService.findAllReceivedPendingFriendRequests(userId);
        List<FoundUserResponseDTO> foundUserResponseDTOS = friendships.stream()
                .map(friendship -> {
                    Optional<Friendship> friendshipOptional = Optional.of(friendship);
                    return UserUtils.getFoundUserResponseDTOByUserAndFriendship(friendship.getRequestedUser(), friendshipOptional);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(foundUserResponseDTOS);
    }



}
