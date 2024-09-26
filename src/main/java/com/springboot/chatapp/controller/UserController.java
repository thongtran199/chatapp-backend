package com.springboot.chatapp.controller;


import com.springboot.chatapp.domain.dto.user.response.UserResponseDTO;
import com.springboot.chatapp.domain.dto.user.response.UserProfileDTO;
import com.springboot.chatapp.domain.entity.User;
import com.springboot.chatapp.mapper.impl.UserMapper;
import com.springboot.chatapp.service.UserService;
import com.springboot.chatapp.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

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
    public ResponseEntity<List<UserResponseDTO>> searchByFullNameContaining(@RequestParam String fullName) {
        List<User> users = userService.findByFullNameContaining(fullName);
        List<UserResponseDTO> userDTOs = users.stream()
                .map(userMapper::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }
}