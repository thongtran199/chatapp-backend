package com.springboot.chatapp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.chatapp.service.SocketService;
import com.springboot.chatapp.model.dto.socket.NewNotificationSentBySocketDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SocketServiceImpl implements SocketService {
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    public SocketServiceImpl(
            SimpMessagingTemplate messagingTemplate,
            ObjectMapper objectMapper) {

        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendNotificationToUser(Long userId, NewNotificationSentBySocketDto newNotificationSentBySocketDto) throws JsonProcessingException {
        messagingTemplate.convertAndSend("/queue/notification-" + userId, objectMapper.writeValueAsString(newNotificationSentBySocketDto));
    }

    @Override
    public void sendMessageToUser(Long userId, NewNotificationSentBySocketDto newNotificationSentBySocketDto) throws JsonProcessingException {
        messagingTemplate.convertAndSend("/queue/message-" + userId, objectMapper.writeValueAsString(newNotificationSentBySocketDto));
    }
}
