package com.springboot.chatapp.service.manager.impl;

import com.springboot.chatapp.model.entity.Friendship;
import com.springboot.chatapp.model.entity.Notification;
import com.springboot.chatapp.model.enums.NotificationType;
import com.springboot.chatapp.service.manager.FriendshipNotificationManager;
import com.springboot.chatapp.model.dto.notification.NotificationRequestDto;
import com.springboot.chatapp.service.NotificationService;
import org.springframework.stereotype.Component;

@Component
public class FriendRequestNotificationHandler implements FriendshipNotificationManager {
    private final NotificationService notificationService;

    public FriendRequestNotificationHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    @Override
    public Notification save(Friendship friendship) {
        NotificationRequestDto notificationRequestDTO = new NotificationRequestDto(
                friendship.getRequestedUser().getUserId(),
                NotificationType.FRIEND_REQUEST_RECEIVED,
                friendship.getFriendshipId()
        );
        return notificationService.save(notificationRequestDTO);
    }
}
