package com.springboot.chatapp.domain.dto.user.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class LatestMessageHistoryResponseDTO {
    private Long messageId;
    private Long messageSenderId;
    private String content;
    private boolean isRead;
    private LocalDateTime sentAt;
}
