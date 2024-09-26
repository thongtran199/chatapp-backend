package com.springboot.chatapp.manager.impl;

import com.springboot.chatapp.domain.entity.Friendship;
import com.springboot.chatapp.domain.entity.Notification;
import com.springboot.chatapp.domain.enumerate.NotificationType;
import com.springboot.chatapp.manager.FriendshipNotificationManager;
import com.springboot.chatapp.payload.notification.NewNotificationDTO;
import com.springboot.chatapp.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FriendAcceptanceNotificationHandler implements FriendshipNotificationManager {
    @Autowired
    private NotificationService notificationService;
    @Override
    public Notification save(Friendship friendship) {
        NewNotificationDTO newNotificationDTO = new NewNotificationDTO(
                friendship.getRequester().getUserId(),
                NotificationType.FRIEND_REQUEST_ACCEPTED,
                friendship.getFriendshipId()
        );
        return notificationService.save(newNotificationDTO);
    }
}
