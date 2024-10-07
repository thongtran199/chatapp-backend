package com.springboot.chatapp.model.dto.notification;

import com.springboot.chatapp.model.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDto {
    private Long userId;
    private NotificationType type;
    private Long referenceId;
}
