package com.springboot.chatapp.service;

import com.springboot.chatapp.domain.dto.user.request.MessageRequestDTO;
import com.springboot.chatapp.domain.dto.user.response.MessageHistoryResponseDTO;
import com.springboot.chatapp.domain.entity.Message;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageService {
    Message save(MessageRequestDTO messageRequestDTO);

    Message findById(Long messageId);

    List<Message> findByMessageSenderAndReceiver(Long senderId, Long receiverId);

    boolean existsById(Long messageId);

    void markMessageAsRead(Long messageId);

    List<Long> findConversationPartnerIds(Long userId);

    Message findLatestMessageBetweenUsers(Long userId,Long partnerId);

    List<MessageHistoryResponseDTO> getChatHistory(Long userId);

}


