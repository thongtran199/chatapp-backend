package com.springboot.chatapp.controller;


import com.springboot.chatapp.domain.dto.user.response.FoundUserResponseDTO;
import com.springboot.chatapp.domain.dto.user.response.UserResponseDTO;
import com.springboot.chatapp.domain.dto.user.response.UserProfileDTO;
import com.springboot.chatapp.domain.entity.Friendship;
import com.springboot.chatapp.domain.entity.User;
import com.springboot.chatapp.domain.enumerate.FriendshipStatus;
import com.springboot.chatapp.mapper.impl.UserMapper;
import com.springboot.chatapp.service.FriendshipService;
import com.springboot.chatapp.service.UserService;
import com.springboot.chatapp.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FriendshipService friendshipService;


    @Autowired
    private UserMapper userMapper;

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        UserResponseDTO userDTO = userMapper.mapToResponseDTO(user);

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        UserResponseDTO userDTO = userMapper.mapToResponseDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfileDTO> getUserProfileById(@PathVariable Long userId) {
        User user = userService.findById(userId);
        UserProfileDTO userProfileDTO = UserUtils.mapToUserProfile(user);
        return ResponseEntity.ok(userProfileDTO);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {
        User user = userService.findById(userId);
        UserResponseDTO userResponseDTO = userMapper.mapToResponseDTO(user);
        return ResponseEntity.ok(userResponseDTO);
    }


    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
        Boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        Boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/search")
    public ResponseEntity<List<FoundUserResponseDTO>> searchByFullNameContaining(@RequestParam String fullName, @RequestParam Long userId) {
        List<User> users = userService.findByFullNameContaining(fullName);
        List<FoundUserResponseDTO> responseDTOs = users.stream()
                .filter(user -> !user.getUserId().equals(userId))
                .map(user -> {
                    FoundUserResponseDTO dto = new FoundUserResponseDTO();
                    dto.setUserId(user.getUserId());
                    dto.setFullName(user.getFullName());
                    dto.setUsername(user.getUsername());
                    dto.setAvatarUrl(user.getAvatarUrl());
                    Optional<Friendship> friendshipOptional = friendshipService.getFriendshipBetweenUsers(userId, user.getUserId());

                    if (friendshipOptional.isPresent()) {
                        Friendship friendship = friendshipOptional.get();
                        dto.setRequesterId(
                                FriendshipStatus.PENDING.equals(friendship.getStatus())
                                ? friendship.getRequester().getUserId() : null);

                        dto.setFriendshipStatus(friendship.getStatus());
                    } else {
                        dto.setRequesterId(null);
                        dto.setFriendshipStatus(FriendshipStatus.NONE);
                    }

                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTOs);
    }

}