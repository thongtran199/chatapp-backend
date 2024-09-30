package com.springboot.chatapp.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.chatapp.payload.notification.NewNotificationSocketDTO;

public interface SocketManager {
    void sendNotificationToUser(Long userId, NewNotificationSocketDTO newNotificationSocketDTO) throws JsonProcessingException;

    void sendMessageToUser(Long userId, NewNotificationSocketDTO newNotificationSocketDTO) throws JsonProcessingException;
}
