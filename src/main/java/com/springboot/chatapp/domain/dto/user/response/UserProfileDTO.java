package com.springboot.chatapp.domain.dto.user.response;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UserProfileDTO {
    private Long userId;
    private String fullName;
    private String username;
    private String email;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
