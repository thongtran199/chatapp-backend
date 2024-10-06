package com.springboot.chatapp.service.impl;

import com.springboot.chatapp.model.dto.message.MessageRequestDto;
import com.springboot.chatapp.model.dto.message.MessageHistoryResponseDto;
import com.springboot.chatapp.model.entity.Friendship;
import com.springboot.chatapp.model.entity.Message;
import com.springboot.chatapp.model.entity.User;
import com.springboot.chatapp.model.exception.ResourceNotFoundException;
import com.springboot.chatapp.repository.MessageRepository;
import com.springboot.chatapp.service.FriendshipService;
import com.springboot.chatapp.service.MessageService;
import com.springboot.chatapp.service.UserService;
import com.springboot.chatapp.utils.MessageUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final FriendshipService friendshipService;

    public MessageServiceImpl(
            MessageRepository messageRepository,
            UserService userService,
            FriendshipService friendshipService) {

        this.messageRepository = messageRepository;
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    @Override
    public Message save(MessageRequestDto messageRequestDTO) {
        User messageSender = userService.findById(messageRequestDTO.getMessageSenderId());
        User messageReceiver = userService.findById(messageRequestDTO.getMessageReceiverId());

        Message message = new Message();
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
    public List<MessageHistoryResponseDto> getChatHistory(Long userId) {
        List<Friendship> friendships = friendshipService.findAcceptedFriendshipsByUserId(userId);
        Map<Long, MessageHistoryResponseDto> combinedMap = new HashMap<>();

        friendships.forEach(friendship -> {
            User user = userId.equals(friendship.getRequester().getUserId())
                    ? friendship.getRequestedUser()
                    : friendship.getRequester();

            combinedMap.put(user.getUserId(), MessageUtils.createMessageHistoryResponseFromUserAndMessage(user, null));
        });

        List<Message> messages = findLatestMessagesByUserId(userId);
        messages.forEach(message -> {
            User user = userId.equals(message.getMessageSender().getUserId())
                    ? message.getMessageReceiver()
                    : message.getMessageSender();

            combinedMap.put(user.getUserId(), MessageUtils.createMessageHistoryResponseFromUserAndMessage(user, message));
        });

        return combinedMap.values().stream()
                .sorted(Comparator.comparing(dto -> Optional.ofNullable(dto.getLatestMessage())
                        .map(MessageHistoryResponseDto.LatestMessageHistoryResponseDTO::getSentAt)
                        .orElse(LocalDateTime.MIN), Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}


