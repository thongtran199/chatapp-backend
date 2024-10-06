package com.springboot.chatapp.controller;


import com.springboot.chatapp.model.dto.user.SearchedUserResponseDto;
import com.springboot.chatapp.model.dto.user.UserResponseDto;
import com.springboot.chatapp.model.dto.user.UserProfileResponseDto;
import com.springboot.chatapp.model.entity.Friendship;
import com.springboot.chatapp.model.entity.User;
import com.springboot.chatapp.utils.mapper.impl.UserMapper;
import com.springboot.chatapp.service.FriendshipService;
import com.springboot.chatapp.service.UserService;
import com.springboot.chatapp.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final UserService userService;
    private final FriendshipService friendshipService;

    public UserController(
            UserService userService,
            FriendshipService friendshipService) {

        this.userService = userService;
        this.friendshipService = friendshipService;
    }


    @Autowired
    private UserMapper userMapper;

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        UserResponseDto userDTO = userMapper.mapToResponseDTO(user);

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        UserResponseDto userDTO = userMapper.mapToResponseDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfileResponseDto> getUserProfileById(@PathVariable Long userId) {
        User user = userService.findById(userId);
        UserProfileResponseDto userProfileResponseDto = UserUtils.mapToUserProfile(user);
        return ResponseEntity.ok(userProfileResponseDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        User user = userService.findById(userId);
        UserResponseDto userResponseDTO = userMapper.mapToResponseDTO(user);
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
    public ResponseEntity<List<SearchedUserResponseDto>> searchByFullNameContaining(@RequestParam String fullName,
                                                                                    @RequestParam Long userId) {
        List<User> users = userService.findByFullNameContaining(fullName);

        List<SearchedUserResponseDto> responseDTOs = users.stream()
                .filter(user -> !user.getUserId().equals(userId))
                .map(user -> {
                    Optional<Friendship> latestFriendship = friendshipService
                            .findLatestFriendshipBetweenUsers(userId, user.getUserId());
                    return UserUtils.getSearchedUserResponseDTOByUserAndFriendship(user, latestFriendship);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/get-me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserResponseDto> getMeByJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.findByUsername(((UserDetails) authentication.getPrincipal()).getUsername());
            return ResponseEntity.ok(userMapper.mapToResponseDTO(user));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


}