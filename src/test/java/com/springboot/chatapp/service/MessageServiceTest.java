package com.springboot.chatapp.service;

import com.springboot.chatapp.model.dto.message.MessageRequestDto;
import com.springboot.chatapp.model.dto.message.MessageHistoryResponseDto;
import com.springboot.chatapp.model.entity.Friendship;
import com.springboot.chatapp.model.entity.Message;
import com.springboot.chatapp.model.entity.User;
import com.springboot.chatapp.model.exception.ResourceNotFoundException;
import com.springboot.chatapp.repository.MessageRepository;
import com.springboot.chatapp.service.impl.MessageServiceImpl;
import com.springboot.chatapp.utils.MessageUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserService userService;

    @Mock
    private FriendshipService friendshipService;

    @InjectMocks
    private MessageServiceImpl messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save() {
        MessageRequestDto messageRequest = new MessageRequestDto();
        messageRequest.setMessageSenderId(1L);
        messageRequest.setMessageReceiverId(2L);
        messageRequest.setContent("Hello!");

        User sender = new User();
        sender.setUserId(1L);
        User receiver = new User();
        receiver.setUserId(2L);

        when(userService.findById(1L)).thenReturn(sender);
        when(userService.findById(2L)).thenReturn(receiver);
        when(messageRepository.save(any(Message.class))).thenReturn(new Message());

        Message result = messageService.save(messageRequest);

        assertNotNull(result);
        verify(userService, times(1)).findById(1L);
        verify(userService, times(1)).findById(2L);
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void findById() {
        Message message = new Message();
        message.setMessageId(1L);

        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

        Message result = messageService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getMessageId());
        verify(messageRepository, times(1)).findById(1L);
    }

    @Test
    void findById_NotFound() {
        when(messageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> messageService.findById(1L));
        verify(messageRepository, times(1)).findById(1L);
    }

    @Test
    void findByMessageSenderAndReceiver() {
        when(messageRepository.findByMessageSenderAndReceiver(1L, 2L)).thenReturn(List.of(new Message()));

        List<Message> result = messageService.findByMessageSenderAndReceiver(1L, 2L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(messageRepository, times(1)).findByMessageSenderAndReceiver(1L, 2L);
    }

    @Test
    void existsById() {
        when(messageRepository.existsById(1L)).thenReturn(true);

        boolean result = messageService.existsById(1L);

        assertTrue(result);
        verify(messageRepository, times(1)).existsById(1L);
    }

    @Test
    void markMessageAsRead() {
        messageService.markMessageAsRead(1L);

        verify(messageRepository, times(1)).markMessageAsRead(1L);
    }

    @Test
    void findLatestMessagesByUserId() {
        when(messageRepository.findLatestMessagesByUserId(1L)).thenReturn(List.of(new Message()));

        List<Message> result = messageService.findLatestMessagesByUserId(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(messageRepository, times(1)).findLatestMessagesByUserId(1L);
    }

    @Test
    void getChatHistory() {
        User requester = new User();
        requester.setUserId(1L);
        User requestedUser = new User();
        requestedUser.setUserId(2L);
        Friendship friendship = new Friendship();
        friendship.setRequester(requester);
        friendship.setRequestedUser(requestedUser);

        when(friendshipService.findAcceptedFriendshipsByUserId(1L)).thenReturn(List.of(friendship));

        Message message = new Message();
        message.setMessageSender(requester);
        message.setMessageReceiver(requestedUser);
        message.setContent("Hello!");
        message.setSentAt(LocalDateTime.now());

        when(messageRepository.findLatestMessagesByUserId(1L)).thenReturn(List.of(message));

        MessageHistoryResponseDto messageHistoryResponseDto = MessageUtils.createMessageHistoryResponseFromUserAndMessage(requestedUser, message);

        List<MessageHistoryResponseDto> result = messageService.getChatHistory(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size()); // You can check if you received the expected response
        assertEquals(requestedUser.getUserId(), result.get(0).getPartnerId()); // Check partner ID
        assertEquals(message.getContent(), result.get(0).getLatestMessage().getContent()); // Check message content
        verify(friendshipService, times(1)).findAcceptedFriendshipsByUserId(1L);
        verify(messageRepository, times(1)).findLatestMessagesByUserId(1L);
    }
}
