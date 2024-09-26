package com.springboot.chatapp.controller;

import com.springboot.chatapp.domain.dto.user.request.FriendshipRequestDTO;
import com.springboot.chatapp.domain.dto.user.response.FriendshipResponseDTO;
import com.springboot.chatapp.domain.dto.user.response.UserResponseDTO;
import com.springboot.chatapp.domain.entity.Friendship;
import com.springboot.chatapp.domain.entity.User;
import com.springboot.chatapp.manager.FriendshipManager;
import com.springboot.chatapp.mapper.impl.FriendshipMapper;
import com.springboot.chatapp.mapper.impl.UserMapper;
import com.springboot.chatapp.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @PostMapping("/request")
    public ResponseEntity<Void> sendFriendRequest(@RequestBody FriendshipRequestDTO friendshipRequestDTO) {
        friendshipManager.saveFriendRequestAndNotification(friendshipRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/accept/{requesterId}/{requestedUserId}")
    public ResponseEntity<Void> acceptFriendRequest(
            @PathVariable Long requesterId, @PathVariable Long requestedUserId) {
        friendshipManager.acceptFriendRequestAndNotification(requesterId, requestedUserId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/decline/{requesterId}/{requestedUserId}")
    public ResponseEntity<Void> declineFriendRequest(
            @PathVariable Long requesterId, @PathVariable Long requestedUserId) {
        friendshipService.declineFriendRequest(requesterId, requestedUserId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/friends/{userId}")
    public ResponseEntity<List<UserResponseDTO>> getAllFriends(@PathVariable Long userId) {
        List<User> friendships = friendshipManager.getAllFriends(userId);
        List<UserResponseDTO> responseDTOs = friendships.stream()
                .map(userMapper::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/accepted/{userId}")
    public ResponseEntity<List<FriendshipResponseDTO>> getAllAcceptedFriends(@PathVariable Long userId) {
        List<Friendship> friendships = friendshipService.findAllAcceptedFriends(userId);
        List<FriendshipResponseDTO> responseDTOs = friendships.stream()
                .map(friendshipMapper::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/sent/{userId}")
    public ResponseEntity<List<FriendshipResponseDTO>> getAllSentFriendRequests(@PathVariable Long userId) {
        List<Friendship> friendships = friendshipService.findAllSentFriendRequests(userId);
        List<FriendshipResponseDTO> responseDTOs = friendships.stream()
                .map(friendshipMapper::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/declined/{userId}")
    public ResponseEntity<List<FriendshipResponseDTO>> getAllDeclinedFriendRequests(@PathVariable Long userId) {
        List<Friendship> friendships = friendshipService.findAllDeclinedFriendRequests(userId);
        List<FriendshipResponseDTO> responseDTOs = friendships.stream()
                .map(friendshipMapper::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @DeleteMapping("/cancel/{requesterId}/{requestedUserId}")
    public ResponseEntity<Void> cancelFriendRequest(
            @PathVariable Long requesterId, @PathVariable Long requestedUserId) {
        friendshipService.cancelFriendRequest(requesterId, requestedUserId);
        return ResponseEntity.noContent().build();
    }



}
