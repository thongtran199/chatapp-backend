package com.springboot.chatapp.domain.dto.user.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResponseDTO {
    private Long messageId;
    private MessageUserResponseDTO messageSender;
    private MessageUserResponseDTO messageReceiver;
    private String content;
    private boolean isRead;
    private LocalDateTime createdAt;
}