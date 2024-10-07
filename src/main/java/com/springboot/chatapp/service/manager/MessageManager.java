package com.springboot.chatapp.service.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.chatapp.model.dto.message.MessageRequestDto;
import com.springboot.chatapp.model.entity.Message;

public interface MessageManager {
        Message saveMessageAndNotification(MessageRequestDto messageRequestDTO) throws JsonProcessingException;
}
