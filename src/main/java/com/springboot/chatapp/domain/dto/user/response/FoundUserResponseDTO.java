package com.springboot.chatapp.domain.dto.user.response;

import com.springboot.chatapp.domain.enumerate.FriendshipStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FoundUserResponseDTO {
    private Long userId;
    private String fullName;
    private String username;
    private String avatarUrl;
    private Long requesterId;
    private FriendshipStatus friendshipStatus;
}