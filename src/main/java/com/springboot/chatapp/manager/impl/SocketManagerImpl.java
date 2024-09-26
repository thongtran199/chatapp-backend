package com.springboot.chatapp.manager.impl;

import com.springboot.chatapp.manager.SocketManager;
import com.springboot.chatapp.payload.notification.NewNotificationSocketDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SocketManagerImpl implements SocketManager {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendNotificationToUser(Long userId, NewNotificationSocketDTO newNotificationSocketDTO) {
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, newNotificationSocketDTO);
    }
}
