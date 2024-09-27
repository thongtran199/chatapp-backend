package com.springboot.chatapp.domain.dto.user.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MessageHistoryResponseDTO {
    private Long userId;
    private String avatarUrl;
    private LatestMessageHistoryResponseDTO latestMessage;
    private String fullName;
    private String username;

}
