package com.springboot.chatapp.model.dto.notification;

import com.springboot.chatapp.model.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationResponseDto {
    private Long notificationId;
    private Long userId;
    private NotificationType type;
    private Long referenceId;
    private boolean isSeen;
    private LocalDateTime createdAt;
}