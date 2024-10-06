package com.springboot.chatapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.chatapp.model.dto.socket.NewNotificationSentBySocketDto;

public interface SocketService {
    void sendNotificationToUser(Long userId, NewNotificationSentBySocketDto newNotificationSentBySocketDto) throws JsonProcessingException;

    void sendMessageToUser(Long userId, NewNotificationSentBySocketDto newNotificationSentBySocketDto) throws JsonProcessingException;
}
