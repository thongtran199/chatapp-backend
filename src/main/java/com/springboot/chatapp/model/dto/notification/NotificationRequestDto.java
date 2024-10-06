package com.springboot.chatapp.model.dto.notification;

import com.springboot.chatapp.model.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NotificationRequestDto {
    private Long userId;
    private NotificationType type;
    private Long referenceId;
}
