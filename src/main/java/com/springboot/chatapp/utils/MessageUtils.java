package com.springboot.chatapp.utils;

import com.springboot.chatapp.model.dto.message.MessageHistoryResponseDto;
import com.springboot.chatapp.model.entity.Message;
import com.springboot.chatapp.model.entity.User;

public class MessageUtils {

    public static MessageHistoryResponseDto createMessageHistoryResponseFromUserAndMessage(User user, Message message) {

        MessageHistoryResponseDto messageHistoryResponseDTO = new MessageHistoryResponseDto();

        messageHistoryResponseDTO.setPartnerId(user.getUserId());
        messageHistoryResponseDTO.setUsername(user.getUsername());
        messageHistoryResponseDTO.setAvatarUrl(user.getAvatarUrl());
        messageHistoryResponseDTO.setFullName(user.getFullName());

        if(message == null)
        {
            messageHistoryResponseDTO.setLatestMessage(null);
            return messageHistoryResponseDTO;
        }

        MessageHistoryResponseDto.LatestMessageHistoryResponseDTO latestMessageHistoryResponseDTO =
                messageHistoryResponseDTO.new LatestMessageHistoryResponseDTO();

        latestMessageHistoryResponseDTO.setMessageId(message.getMessageId());
        latestMessageHistoryResponseDTO.setRead(message.isRead());
        latestMessageHistoryResponseDTO.setSentAt(message.getSentAt());
        latestMessageHistoryResponseDTO.setContent(message.getContent());
        latestMessageHistoryResponseDTO.setMessageSenderId(message.getMessageSender().getUserId());

        messageHistoryResponseDTO.setLatestMessage(latestMessageHistoryResponseDTO);

        return messageHistoryResponseDTO;
    }
}
