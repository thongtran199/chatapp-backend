package com.springboot.chatapp.controller;

import com.springboot.chatapp.domain.dto.user.request.MessageRequestDTO;
import com.springboot.chatapp.domain.dto.user.response.MessageResponseDTO;
import com.springboot.chatapp.domain.entity.Message;
import com.springboot.chatapp.manager.MessageManager;
import com.springboot.chatapp.mapper.impl.MessageMapper;
import com.springboot.chatapp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // Gửi tin nhắn
    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody MessageRequestDTO messageRequestDTO) {
        messageManager.saveMessageAndNotification(messageRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<MessageResponseDTO> getMessageById(@PathVariable Long messageId) {
        Message message = messageService.findById(messageId);
        MessageResponseDTO messageResponseDTO = messageMapper.mapToResponseDTO(message);
        return ResponseEntity.ok(messageResponseDTO);
    }

    @GetMapping("/conversation/{senderId}/{receiverId}")
    public ResponseEntity<List<MessageResponseDTO>> getMessagesBetweenUsers(
            @PathVariable Long senderId, @PathVariable Long receiverId) {
        List<Message> messages = messageService.findByMessageSenderAndReceiver(senderId, receiverId);
        List<MessageResponseDTO> messageResponseDTOs = messages.stream()
                .map(messageMapper::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messageResponseDTOs);
    }

    @PostMapping("/mark-read/{messageId}")
    public ResponseEntity<Void> markMessageAsRead(@PathVariable Long messageId) {
        messageService.markMessageAsRead(messageId);
        return ResponseEntity.ok().build();
    }
}