package com.springboot.chatapp.service.impl;

import com.springboot.chatapp.domain.dto.user.request.MessageRequestDTO;
import com.springboot.chatapp.domain.dto.user.response.LatestMessageHistoryResponseDTO;
import com.springboot.chatapp.domain.dto.user.response.MessageHistoryResponseDTO;
import com.springboot.chatapp.domain.entity.Friendship;
import com.springboot.chatapp.domain.entity.Message;
import com.springboot.chatapp.domain.entity.User;
import com.springboot.chatapp.exception.ResourceNotFoundException;
import com.springboot.chatapp.repository.MessageRepository;
import com.springboot.chatapp.repository.UserRepository;
import com.springboot.chatapp.service.FriendshipService;
import com.springboot.chatapp.service.MessageService;
import com.springboot.chatapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendshipService friendshipService;

    @Override
    public Message save(MessageRequestDTO messageRequestDTO) {
        Message message = new Message();
        User messageSender = userService.findById(messageRequestDTO.getMessageSenderId());
        User messageReceiver = userService.findById(messageRequestDTO.getMessageReceiverId());
        message.setMessageSender(messageSender);
        message.setMessageReceiver(messageReceiver);
        message.setContent(messageRequestDTO.getContent());

        return messageRepository.save(message);
    }

    @Override
    public Message findById(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message", "messageId", messageId));

    }

    @Override
    public List<Message> findByMessageSenderAndReceiver(Long userId1, Long userId2) {
        return messageRepository.findByMessageSenderAndReceiver(userId1, userId2);
    }

    @Override
    public boolean existsById(Long messageId) {
        return messageRepository.existsById(messageId);
    }

    @Override
    public void markMessageAsRead(Long messageId) {
        messageRepository.markMessageAsRead(messageId);
    }

    @Override
    public List<Message> findLatestMessagesByUserId(Long userId) {
        return messageRepository.findLatestMessagesByUserId(userId);
    }

    @Override
    public List<MessageHistoryResponseDTO> getChatHistory(Long userId) {

        List<Friendship> friendships = friendshipService.findAcceptedFriendshipsByUserId(userId);
        List<MessageHistoryResponseDTO> messageHistoryResponseDTOS = new ArrayList<>();
        for(Friendship friendship: friendships){
            MessageHistoryResponseDTO messageHistoryResponseDTO = new MessageHistoryResponseDTO();
            User user;
            if(friendship.getRequester().getUserId() == userId)
            {
                user = friendship.getRequestedUser();
            }
            else {
                user = friendship.getRequester();
            }
            messageHistoryResponseDTO.setPartnerId(user.getUserId());
            messageHistoryResponseDTO.setUsername(user.getUsername());
            messageHistoryResponseDTO.setAvatarUrl(user.getAvatarUrl());
            messageHistoryResponseDTO.setFullName(user.getFullName());
            messageHistoryResponseDTO.setLatestMessage(null);
            messageHistoryResponseDTOS.add(messageHistoryResponseDTO);
        }

        List<Message> messages = findLatestMessagesByUserId(userId);
        List<MessageHistoryResponseDTO> messageHistoryResponseDTOS1 = messages.stream().map(message -> {
            User user;
            MessageHistoryResponseDTO messageHistoryResponseDTO = new MessageHistoryResponseDTO();
            if(message.getMessageSender().getUserId() == userId)
            {
                user =  message.getMessageReceiver();
            }
            else
            {
                user = message.getMessageSender();
            }
            messageHistoryResponseDTO.setPartnerId(user.getUserId());
            messageHistoryResponseDTO.setUsername(user.getUsername());
            messageHistoryResponseDTO.setAvatarUrl(user.getAvatarUrl());
            messageHistoryResponseDTO.setFullName(user.getFullName());

            LatestMessageHistoryResponseDTO latestMessageHistoryResponseDTO = new LatestMessageHistoryResponseDTO();
            latestMessageHistoryResponseDTO.setMessageId(message.getMessageId());
            latestMessageHistoryResponseDTO.setRead(message.isRead());
            latestMessageHistoryResponseDTO.setSentAt(message.getSentAt());
            latestMessageHistoryResponseDTO.setContent(message.getContent());
            latestMessageHistoryResponseDTO.setMessageSenderId(message.getMessageSender().getUserId());

            messageHistoryResponseDTO.setLatestMessage(latestMessageHistoryResponseDTO);
            return messageHistoryResponseDTO;

        }).collect(Collectors.toList());

        Map<Long, MessageHistoryResponseDTO> combinedMap = new HashMap<>();

        for (MessageHistoryResponseDTO dto : messageHistoryResponseDTOS) {
            combinedMap.put(dto.getPartnerId(), dto);
        }

        for (MessageHistoryResponseDTO dto : messageHistoryResponseDTOS1) {
            combinedMap.put(dto.getPartnerId(), dto);
        }
        List<MessageHistoryResponseDTO> history = new ArrayList<>(combinedMap.values());

        return history.stream()
                .sorted(Comparator.comparing(dto -> {
                    if (dto.getLatestMessage() != null) {
                        return dto.getLatestMessage().getSentAt();
                    }
                    return LocalDateTime.MIN;
                }, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }


}


