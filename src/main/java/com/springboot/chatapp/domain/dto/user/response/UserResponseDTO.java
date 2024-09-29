package com.springboot.chatapp.domain.dto.user.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDTO {
    private Long userId;
    private String fullName;
    private String username;
    private String avatarUrl;
    private String email;
}