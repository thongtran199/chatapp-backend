package com.springboot.chatapp.domain.dto.user.request;

import lombok.Data;

@Data
public class FriendshipRequestDTO {
    private Long requesterId;
    private Long requestedUserId;
}
