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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
    public List<Message> findByMessageSenderAndReceiver(Long senderId, Long receiverId) {
        return messageRepository.findByMessageSenderAndReceiver(senderId, receiverId);
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
    public List<Long> findConversationPartnerIds(Long userId) {
        return messageRepository.findConversationPartnerIds(userId);
    }

    @Override
    public Message findLatestMessageBetweenUsers(Long userId, Long partnerId) {
        return messageRepository.findLatestMessageBetweenUsers(userId, partnerId);
    }

    @Override
    public List<MessageHistoryResponseDTO> getChatHistory(Long userId) {
        List<Friendship> friendships = friendshipService.findAllFriendsByUserId(userId);
        if (friendships.isEmpty()) {
            return new ArrayList<>();
        }
        List<MessageHistoryResponseDTO> history = new ArrayList<>();

        for (Friendship friendship : friendships) {
            MessageHistoryResponseDTO dto = new MessageHistoryResponseDTO();
            Long partnerId;
            if (friendship.getRequester().getUserId().equals(userId)) {
                partnerId = friendship.getRequestedUser().getUserId();
                dto.setFullName(friendship.getRequestedUser().getFullName());
                dto.setUsername(friendship.getRequestedUser().getUsername());
                dto.setAvatarUrl(friendship.getRequestedUser().getAvatarUrl());
            } else {
                partnerId = friendship.getRequester().getUserId();
                dto.setFullName(friendship.getRequester().getFullName());
                dto.setUsername(friendship.getRequester().getUsername());
                dto.setAvatarUrl(friendship.getRequester().getAvatarUrl());
            }

            dto.setUserId(partnerId);

            Message latestMessage = messageRepository.findLatestMessageBetweenUsers(userId, partnerId);

            if (latestMessage != null) {

                LatestMessageHistoryResponseDTO latestMessageDTO = new LatestMessageHistoryResponseDTO();
                latestMessageDTO.setCreatedAt(latestMessage.getSentAt());
                latestMessageDTO.setContent(latestMessage.getContent());

                dto.setLatestMessage(latestMessageDTO);

            } else {
                dto.setLatestMessage(null);
            }

            history.add(dto);
        }

        return history.stream()
                .sorted(Comparator.comparing(dto -> {
                    if (dto.getLatestMessage() != null) {
                        return dto.getLatestMessage().getCreatedAt();
                    }
                    return LocalDateTime.MIN;
                }, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }


}


