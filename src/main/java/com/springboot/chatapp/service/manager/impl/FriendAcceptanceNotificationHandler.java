package com.springboot.chatapp.service.manager.impl;

import com.springboot.chatapp.model.entity.Friendship;
import com.springboot.chatapp.model.entity.Notification;
import com.springboot.chatapp.model.enums.NotificationType;
import com.springboot.chatapp.service.manager.FriendshipNotificationManager;
import com.springboot.chatapp.model.dto.notification.NotificationRequestDto;
import com.springboot.chatapp.service.NotificationService;
import org.springframework.stereotype.Component;

@Component
public class FriendAcceptanceNotificationHandler implements FriendshipNotificationManager {
    private final NotificationService notificationService;

    public FriendAcceptanceNotificationHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    @Override
    public Notification save(Friendship friendship) {
        NotificationRequestDto notificationRequestDTO = new NotificationRequestDto(
                friendship.getRequester().getUserId(),
                NotificationType.FRIEND_REQUEST_ACCEPTED,
                friendship.getFriendshipId()
        );
        return notificationService.save(notificationRequestDTO);
    }
}
