package com.springboot.chatapp.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.chatapp.domain.dto.user.request.FriendshipRequestDTO;
import com.springboot.chatapp.domain.dto.user.request.MessageRequestDTO;

public interface MessageManager {
        void saveMessageAndNotification(MessageRequestDTO messageRequestDTO) throws JsonProcessingException;


}
