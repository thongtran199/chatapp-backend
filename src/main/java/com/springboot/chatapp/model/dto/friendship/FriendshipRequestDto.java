package com.springboot.chatapp.model.dto.friendship;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FriendshipRequestDto {
    private Long requesterId;
    private Long requestedUserId;
}
