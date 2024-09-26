package com.springboot.chatapp.payload.notification;

import com.springboot.chatapp.domain.enumerate.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewPasswordResetTokenDTO {
    private Long userId;
    private String resetToken;
    private LocalDateTime expiresAt;
}