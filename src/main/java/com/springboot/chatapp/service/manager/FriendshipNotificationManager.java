package com.springboot.chatapp.service.manager;

import com.springboot.chatapp.model.entity.Friendship;
import com.springboot.chatapp.model.entity.Notification;

public interface FriendshipNotificationManager {
        Notification save(Friendship friendship);
}
