package com.springboot.chatapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.chatapp.model.dto.message.MessageRequestDto;
import com.springboot.chatapp.model.dto.message.MessageHistoryResponseDto;
import com.springboot.chatapp.model.dto.message.MessageResponseDto;
import com.springboot.chatapp.model.entity.Message;
import com.springboot.chatapp.model.exception.ChatAppAPIException;
import com.springboot.chatapp.service.manager.MessageManager;
import com.springboot.chatapp.utils.mapper.impl.MessageMapper;
import com.springboot.chatapp.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    private final MessageManager messageManager;
    private final MessageService messageService;
    private final MessageMapper messageMapper;

    public MessageController(
            MessageManager messageManager,
            MessageService messageService,
            MessageMapper messageMapper) {

        this.messageManager = messageManager;
        this.messageService = messageService;
        this.messageMapper = messageMapper;
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@Valid @RequestBody MessageRequestDto messageRequestDTO) {
        try {
            messageManager.saveMessageAndNotification(messageRequestDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (JsonProcessingException e) {
            throw new ChatAppAPIException(HttpStatus.EXPECTATION_FAILED, "Can't process JSON of NotificationSocketDTO");
        }

    }

    @GetMapping("/{messageId}")
    public ResponseEntity<MessageResponseDto> getMessageById(@PathVariable Long messageId) {
        Message message = messageService.findById(messageId);
        MessageResponseDto messageResponseDTO = messageMapper.mapToResponseDTO(message);
        return ResponseEntity.ok(messageResponseDTO);
    }

    @GetMapping("/conversation/{userId1}/{userId2}")
    public ResponseEntity<List<MessageResponseDto>> getConversation(
            @PathVariable Long userId1, @PathVariable Long userId2) {
        List<Message> messages = messageService.findByMessageSenderAndReceiver(userId1, userId2);
        List<MessageResponseDto> messageResponseDtos = messages.stream()
                .map(messageMapper::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messageResponseDtos);
    }

    @GetMapping("/chat-history/{userId}")
    public ResponseEntity<List<MessageHistoryResponseDto>> getMessageHistory(
            @PathVariable Long userId) {
        List<MessageHistoryResponseDto> messageHistoryResponseDtos = messageService.getChatHistory(userId);
        return ResponseEntity.ok(messageHistoryResponseDtos);
    }

    @PostMapping("/mark-read/{messageId}")
    public ResponseEntity<Void> markMessageAsRead(@PathVariable Long messageId) {
        messageService.markMessageAsRead(messageId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
