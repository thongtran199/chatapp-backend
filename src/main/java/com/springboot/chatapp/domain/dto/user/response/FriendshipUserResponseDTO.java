package com.springboot.chatapp.domain.dto.user.response;

import lombok.Data;

@Data
public class FriendshipUserResponseDTO {
    private Long userId;
    private String fullName;
    private String username;
    private String avatarUrl;
}