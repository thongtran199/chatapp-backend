package com.springboot.chatapp.service.manager.factory;

import com.springboot.chatapp.model.enums.NotificationType;
import com.springboot.chatapp.service.manager.FriendshipNotificationManager;
import com.springboot.chatapp.service.manager.impl.FriendAcceptanceNotificationHandler;
import com.springboot.chatapp.service.manager.impl.FriendRequestNotificationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FriendshipNotificationFactory {
    private FriendRequestNotificationHandler friendRequestNotificationHandler;
    private FriendAcceptanceNotificationHandler friendAcceptanceNotificationHandler;

    @Autowired
    public FriendshipNotificationFactory(FriendRequestNotificationHandler friendRequestNotificationHandler,
                                         FriendAcceptanceNotificationHandler friendAcceptanceNotificationHandler) {
        this.friendRequestNotificationHandler = friendRequestNotificationHandler;
        this.friendAcceptanceNotificationHandler = friendAcceptanceNotificationHandler;
    }

    public FriendshipNotificationManager createNotificationHandler(NotificationType notificationType) {
        if (notificationType == NotificationType.FRIEND_REQUEST_RECEIVED) {
            return friendRequestNotificationHandler;
        } else if (notificationType == NotificationType.FRIEND_REQUEST_ACCEPTED) {
            return friendAcceptanceNotificationHandler;
        }
        throw new IllegalArgumentException("No handler for this notification type");
    }
}
