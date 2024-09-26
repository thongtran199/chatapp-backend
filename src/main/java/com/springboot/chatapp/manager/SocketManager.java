package com.springboot.chatapp.manager;

import com.springboot.chatapp.payload.notification.NewNotificationSocketDTO;

public interface SocketManager {
    void sendNotificationToUser(Long userId, NewNotificationSocketDTO newNotificationSocketDTO);
}
