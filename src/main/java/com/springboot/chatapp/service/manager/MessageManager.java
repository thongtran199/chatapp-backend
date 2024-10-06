package com.springboot.chatapp.service.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.chatapp.model.dto.message.MessageRequestDto;

public interface MessageManager {
        void saveMessageAndNotification(MessageRequestDto messageRequestDTO) throws JsonProcessingException;
}
