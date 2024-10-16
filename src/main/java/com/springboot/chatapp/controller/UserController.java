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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "User Management", description = "Endpoints for managing users")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final FriendshipService friendshipService;
    private final UserMapper userMapper;

    public UserController(
            UserMapper userMapper,
            UserService userService,
            FriendshipService friendshipService) {

        this.userService = userService;
        this.friendshipService = friendshipService;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Get user by username", description = "Provide a username to look up a specific user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            })
    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDto> getUserByUsername(
            @Parameter(description = "Username of the user to retrieve", required = true) @PathVariable String username) {
        User user = userService.findByUsername(username);
        UserResponseDto userDTO = userMapper.mapToResponseDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @Operation(summary = "Get user by email", description = "Provide an email to look up a specific user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            })
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(
            @Parameter(description = "Email of the user to retrieve", required = true) @PathVariable String email) {
        User user = userService.findByEmail(email);
        UserResponseDto userDTO = userMapper.mapToResponseDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @Operation(summary = "Get user profile by user ID", description = "Retrieve the profile of a user by user ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User profile retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            })
    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfileResponseDto> getUserProfileById(
            @Parameter(description = "ID of the user to retrieve the profile for", required = true) @PathVariable Long userId) {
        User user = userService.findById(userId);
        UserProfileResponseDto userProfileResponseDto = UserUtils.mapToUserProfile(user);
        return ResponseEntity.ok(userProfileResponseDto);
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a user by user ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(
            @Parameter(description = "ID of the user to retrieve", required = true) @PathVariable Long userId) {
        User user = userService.findById(userId);
        UserResponseDto userResponseDTO = userMapper.mapToResponseDTO(user);
        return ResponseEntity.ok(userResponseDTO);
    }

    @Operation(summary = "Check if username exists", description = "Check if a username is already in use",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Check completed successfully"),
                    @ApiResponse(responseCode = "404", description = "Username not found")
            })
    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(
            @Parameter(description = "Username to check for existence", required = true) @PathVariable String username) {
        Boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "Check if email exists", description = "Check if an email is already in use",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Check completed successfully"),
                    @ApiResponse(responseCode = "404", description = "Email not found")
            })
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(
            @Parameter(description = "Email to check for existence", required = true) @PathVariable String email) {
        Boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "Search users by full name", description = "Search for users whose full name contains the provided string",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Search completed successfully"),
                    @ApiResponse(responseCode = "404", description = "No users found")
            })
    @GetMapping("/search")
    public ResponseEntity<List<SearchedUserResponseDto>> searchByFullNameContaining(
            @Parameter(description = "Full name to search for", required = true) @RequestParam String fullName,
            @Parameter(description = "ID of the user performing the search", required = true) @RequestParam Long userId) {
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

    @Operation(summary = "Get current user details", description = "Retrieve the details of the currently authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Current user details retrieved successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            })
    @GetMapping("/get-me")
    public ResponseEntity<UserResponseDto> getMeByJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.findByUsername(((UserDetails) authentication.getPrincipal()).getUsername());
            return ResponseEntity.ok(userMapper.mapToResponseDTO(user));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
