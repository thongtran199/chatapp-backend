package com.springboot.chatapp.model.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private Long userId;
    private String fullName;
    private String username;
    private String avatarUrl;
    private String email;
}