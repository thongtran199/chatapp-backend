package com.springboot.chatapp.payload.notification;

import com.springboot.chatapp.domain.enumerate.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewNotificationSocketDTO {
        private Long notificationId;
        private NotificationType type;
        private Long referenceId;
        private boolean isSeen;
        private LocalDateTime createdAt;
        private String header;
        private String content;
        private String avatarUrl;
        private Long partnerId;
}
