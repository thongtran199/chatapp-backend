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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Message Management", description = "Endpoints for managing messages")
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

    @Operation(summary = "Send a message", description = "Sends a new message",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Message sent successfully"),
                    @ApiResponse(responseCode = "417", description = "Failed to process message due to JSON error")
            })
    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(
            @Parameter(description = "Message Request DTO", required = true) @Valid @RequestBody MessageRequestDto messageRequestDTO) {
        try {
            messageManager.saveMessageAndNotification(messageRequestDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            throw new ChatAppAPIException(HttpStatus.EXPECTATION_FAILED, "Can't process JSON of NotificationSocketDTO");
        }
    }

    @Operation(summary = "Get a message by ID", description = "Retrieve a message by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Message retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Message not found")
            })
    @GetMapping("/{messageId}")
    public ResponseEntity<MessageResponseDto> getMessageById(
            @Parameter(description = "ID of the message to retrieve", required = true) @PathVariable Long messageId) {
        Message message = messageService.findById(messageId);
        MessageResponseDto messageResponseDTO = messageMapper.mapToResponseDTO(message);
        return ResponseEntity.ok(messageResponseDTO);
    }

    @Operation(summary = "Get conversation between two users", description = "Retrieve messages between two users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conversation retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Conversation not found")
            })
    @GetMapping("/conversation/{userId1}/{userId2}")
    public ResponseEntity<List<MessageResponseDto>> getConversation(
            @Parameter(description = "ID of the first user", required = true) @PathVariable Long userId1,
            @Parameter(description = "ID of the second user", required = true) @PathVariable Long userId2) {
        List<Message> messages = messageService.findByMessageSenderAndReceiver(userId1, userId2);
        List<MessageResponseDto> messageResponseDtos = messages.stream()
                .map(messageMapper::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messageResponseDtos);
    }

    @Operation(summary = "Get message history", description = "Retrieve chat history for a user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Chat history retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Chat history not found")
            })
    @GetMapping("/chat-history/{userId}")
    public ResponseEntity<List<MessageHistoryResponseDto>> getMessageHistory(
            @Parameter(description = "ID of the user to retrieve chat history for", required = true) @PathVariable Long userId) {
        List<MessageHistoryResponseDto> messageHistoryResponseDtos = messageService.getChatHistory(userId);
        return ResponseEntity.ok(messageHistoryResponseDtos);
    }

    @Operation(summary = "Mark message as read", description = "Marks a message as read",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Message marked as read"),
                    @ApiResponse(responseCode = "404", description = "Message not found")
            })
    @PostMapping("/mark-read/{messageId}")
    public ResponseEntity<Void> markMessageAsRead(
            @Parameter(description = "ID of the message to mark as read", required = true) @PathVariable Long messageId) {
        messageService.markMessageAsRead(messageId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
