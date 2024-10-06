package com.springboot.chatapp.model.dto.friendship;

import com.springboot.chatapp.model.enums.FriendshipStatus;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
public class FriendshipResponseDto {
    private Long friendshipId;
    private FriendshipUserResponseDTO requester;
    private FriendshipUserResponseDTO requestedUser;
    private FriendshipStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Setter
    @Getter
    private class FriendshipUserResponseDTO {
        private Long userId;
        private String fullName;
        private String username;
        private String avatarUrl;
    }

}