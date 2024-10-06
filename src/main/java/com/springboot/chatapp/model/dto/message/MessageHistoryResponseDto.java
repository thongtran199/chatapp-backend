package com.springboot.chatapp.model.dto.message;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class MessageHistoryResponseDto {
    private Long partnerId;
    private String avatarUrl;
    private LatestMessageHistoryResponseDTO latestMessage;
    private String fullName;
    private String username;

    @Getter
    @Setter
    public class LatestMessageHistoryResponseDTO {
        private Long messageId;
        private Long messageSenderId;
        private String content;
        private boolean isRead;
        private LocalDateTime sentAt;
    }
}
