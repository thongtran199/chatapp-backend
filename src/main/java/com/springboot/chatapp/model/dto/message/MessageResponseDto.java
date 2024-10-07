package com.springboot.chatapp.model.dto.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Access;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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