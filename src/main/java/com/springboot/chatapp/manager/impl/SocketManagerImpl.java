package com.springboot.chatapp.manager.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.chatapp.manager.SocketManager;
import com.springboot.chatapp.payload.notification.NewNotificationSocketDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SocketManagerImpl implements SocketManager {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void sendNotificationToUser(Long userId, NewNotificationSocketDTO newNotificationSocketDTO) throws JsonProcessingException {
        messagingTemplate.convertAndSend("/queue/notification-" + userId, objectMapper.writeValueAsString(newNotificationSocketDTO));
    }

    @Override
    public void sendMessageToUser(Long userId, NewNotificationSocketDTO newNotificationSocketDTO) throws JsonProcessingException {
        messagingTemplate.convertAndSend("/queue/message-" + userId, objectMapper.writeValueAsString(newNotificationSocketDTO));
    }
}
