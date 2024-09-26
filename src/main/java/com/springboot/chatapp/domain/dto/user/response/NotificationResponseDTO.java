package com.springboot.chatapp.domain.dto.user.response;

import com.springboot.chatapp.domain.enumerate.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponseDTO {
    private Long notificationId;
    private Long userId;
    private NotificationType type;
    private Long referenceId;
    private boolean isSeen;
    private LocalDateTime createdAt;
}