package com.springboot.chatapp.model.dto.user;

import com.springboot.chatapp.model.enums.FriendshipStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchedUserResponseDto {
    private Long userId;
    private String fullName;
    private String username;
    private String avatarUrl;
    private FriendshipSearchedUserResponseDto friendshipSearchedUserResponseDto;

    @Setter
    @Getter
    public class FriendshipSearchedUserResponseDto {
        private Long friendshipId;
        private FriendshipStatus friendshipStatus;
        private Long requesterId;
        private Long requestedUserId;
    }
}