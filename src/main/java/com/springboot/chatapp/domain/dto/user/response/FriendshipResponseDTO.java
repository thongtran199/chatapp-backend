package com.springboot.chatapp.domain.dto.user.response;

import com.springboot.chatapp.domain.enumerate.FriendshipStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendshipResponseDTO {
    private Long friendshipId;
    private FriendshipUserResponseDTO requester;
    private FriendshipUserResponseDTO requestedUser;
    private FriendshipStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}