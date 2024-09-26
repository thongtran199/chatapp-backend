package com.springboot.chatapp.service.impl;

import com.springboot.chatapp.domain.dto.user.request.MessageRequestDTO;
import com.springboot.chatapp.domain.entity.Message;
import com.springboot.chatapp.domain.entity.User;
import com.springboot.chatapp.exception.ResourceNotFoundException;
import com.springboot.chatapp.repository.MessageRepository;
import com.springboot.chatapp.repository.UserRepository;
import com.springboot.chatapp.service.MessageService;
import com.springboot.chatapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

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
}
