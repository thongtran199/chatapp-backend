package com.springboot.chatapp.model.dto.message;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class MessageResponseDto {
    private Long messageId;
    private MessageUserResponseDTO messageSender;
    private MessageUserResponseDTO messageReceiver;
    private String content;
    private boolean isRead;
    private LocalDateTime sentAt;

    @Getter
    @Setter
    private class MessageUserResponseDTO {
        private Long userId;
    }
}