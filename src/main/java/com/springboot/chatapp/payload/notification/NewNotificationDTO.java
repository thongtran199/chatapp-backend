package com.springboot.chatapp.payload.notification;

import com.springboot.chatapp.domain.enumerate.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewNotificationDTO {
    private Long userId;
    private NotificationType type;
    private Long referenceId;
}
