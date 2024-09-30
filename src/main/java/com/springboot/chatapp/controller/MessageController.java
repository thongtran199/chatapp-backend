package com.springboot.chatapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.chatapp.domain.dto.user.request.MessageRequestDTO;
import com.springboot.chatapp.domain.dto.user.response.MessageHistoryResponseDTO;
import com.springboot.chatapp.domain.dto.user.response.MessageResponseDTO;
import com.springboot.chatapp.domain.entity.Message;
import com.springboot.chatapp.exception.ChatAppAPIException;
import com.springboot.chatapp.manager.MessageManager;
import com.springboot.chatapp.mapper.impl.MessageMapper;
import com.springboot.chatapp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Autowired
    private MessageManager messageManager;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageMapper messageMapper;

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody MessageRequestDTO messageRequestDTO) {
        try
        {
            messageManager.saveMessageAndNotification(messageRequestDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (JsonProcessingException e)
        {
            throw new ChatAppAPIException(HttpStatus.EXPECTATION_FAILED, "Can't process JSON of NotificationSocketDTO");
        }

    }

    @GetMapping("/{messageId}")
    public ResponseEntity<MessageResponseDTO> getMessageById(@PathVariable Long messageId) {
        Message message = messageService.findById(messageId);
        MessageResponseDTO messageResponseDTO = messageMapper.mapToResponseDTO(message);
        return ResponseEntity.ok(messageResponseDTO);
    }

    @GetMapping("/conversation/{userId1}/{userId2}")
    public ResponseEntity<List<MessageResponseDTO>> getConversation(
            @PathVariable Long userId1, @PathVariable Long userId2) {
        List<Message> messages = messageService.findByMessageSenderAndReceiver(userId1, userId2);
        List<MessageResponseDTO> messageResponseDTOs = messages.stream()
                .map(messageMapper::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messageResponseDTOs);
    }

    @GetMapping("/chat-history/{userId}")
    public ResponseEntity<List<MessageHistoryResponseDTO>> getMessageHistory(
            @PathVariable Long userId) {
        List<MessageHistoryResponseDTO> messageHistoryResponseDTOS = messageService.getChatHistory(userId);
        return ResponseEntity.ok(messageHistoryResponseDTOS);
    }

    @PostMapping("/mark-read/{messageId}")
    public ResponseEntity<Void> markMessageAsRead(@PathVariable Long messageId) {
        messageService.markMessageAsRead(messageId);
        return ResponseEntity.ok().build();
    }
}
