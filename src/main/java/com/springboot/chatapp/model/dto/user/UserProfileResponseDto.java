package com.springboot.chatapp.model.dto.user;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserProfileResponseDto {
    private Long userId;
    private String fullName;
    private String username;
    private String email;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
