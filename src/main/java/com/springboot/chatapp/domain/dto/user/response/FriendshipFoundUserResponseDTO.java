package com.springboot.chatapp.domain.dto.user.response;

import com.springboot.chatapp.domain.entity.Friendship;
import com.springboot.chatapp.domain.enumerate.FriendshipStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class FriendshipFoundUserResponseDTO {
    private Long friendshipId;
    private FriendshipStatus friendshipStatus;
    private Long requesterId;
    private Long requestedUserId;
}