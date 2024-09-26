package com.springboot.chatapp.manager;

import com.springboot.chatapp.domain.entity.Friendship;
import com.springboot.chatapp.domain.entity.Notification;

public interface FriendshipNotificationManager {
        Notification save(Friendship friendship);
}
