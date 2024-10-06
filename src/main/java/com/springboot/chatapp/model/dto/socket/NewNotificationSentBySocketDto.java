package com.springboot.chatapp.model.dto.socket;

import com.springboot.chatapp.model.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class NewNotificationSentBySocketDto {
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
