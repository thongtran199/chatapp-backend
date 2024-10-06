package com.springboot.chatapp.service;

import com.springboot.chatapp.model.dto.message.MessageRequestDto;
import com.springboot.chatapp.model.dto.message.MessageHistoryResponseDto;
import com.springboot.chatapp.model.entity.Message;

import java.util.List;

public interface MessageService {
    Message save(MessageRequestDto messageRequestDTO);

    Message findById(Long messageId);

    List<Message> findByMessageSenderAndReceiver(Long userId1, Long userId2);

    boolean existsById(Long messageId);

    void markMessageAsRead(Long messageId);

    List<Message> findLatestMessagesByUserId(Long userId);

    List<MessageHistoryResponseDto> getChatHistory(Long userId);

}


