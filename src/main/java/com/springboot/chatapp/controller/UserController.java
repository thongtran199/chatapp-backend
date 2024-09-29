package com.springboot.chatapp.controller;


import com.springboot.chatapp.domain.dto.user.response.FoundUserResponseDTO;
import com.springboot.chatapp.domain.dto.user.response.UserResponseDTO;
import com.springboot.chatapp.domain.dto.user.response.UserProfileDTO;
import com.springboot.chatapp.domain.entity.Friendship;
import com.springboot.chatapp.domain.entity.User;
import com.springboot.chatapp.mapper.impl.UserMapper;
import com.springboot.chatapp.service.FriendshipService;
import com.springboot.chatapp.service.UserService;
import com.springboot.chatapp.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
                    Optional<Friendship> latestFriendship = friendshipService
                            .findLatestFriendshipBetweenUsers(userId, user.getUserId());
                    return UserUtils.getFoundUserResponseDTOByUserAndFriendship(user, latestFriendship);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/get-me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserResponseDTO> getMeByJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.findByUsername(((UserDetails) authentication.getPrincipal()).getUsername());
            return ResponseEntity.ok(userMapper.mapToResponseDTO(user));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


}