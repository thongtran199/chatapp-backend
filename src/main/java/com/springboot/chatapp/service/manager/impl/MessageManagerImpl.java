package com.springboot.chatapp.service.manager.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.chatapp.model.dto.message.MessageRequestDto;
import com.springboot.chatapp.model.entity.Message;
import com.springboot.chatapp.model.entity.Notification;
import com.springboot.chatapp.model.entity.User;
import com.springboot.chatapp.model.enums.NotificationType;
import com.springboot.chatapp.service.manager.MessageManager;
import com.springboot.chatapp.service.SocketService;
import com.springboot.chatapp.model.dto.notification.NotificationRequestDto;
import com.springboot.chatapp.model.dto.socket.NewNotificationSentBySocketDto;
import com.springboot.chatapp.service.MessageService;
import com.springboot.chatapp.service.NotificationService;
import com.springboot.chatapp.service.UserService;
import com.springboot.chatapp.utils.NotificationUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MessageManagerImpl implements MessageManager {
    private final MessageService messageService;
    private final NotificationService notificationService;
    private final SocketService socketService;
    private final UserService userService;

    public MessageManagerImpl(
            MessageService messageService,
            NotificationService notificationService,
            SocketService socketService,
            UserService userService) {

        this.messageService = messageService;
        this.notificationService = notificationService;
        this.socketService = socketService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void saveMessageAndNotification(MessageRequestDto messageRequestDTO) throws JsonProcessingException {
        Message message = messageService.save(messageRequestDTO);
        User receiver = message.getMessageReceiver();

        NotificationRequestDto notificationRequestDTO = new NotificationRequestDto(
                message.getMessageReceiver().getUserId(),
                NotificationType.MESSAGE,
                message.getMessageId()
        );
        Notification notification = notificationService.save(notificationRequestDTO);
        User sender = userService.findById(messageRequestDTO.getMessageSenderId());

        NewNotificationSentBySocketDto newNotificationSentBySocketDto = NotificationUtils.createNotificationSocketDTO(sender, notification, messageRequestDTO.getContent());
        socketService.sendMessageToUser(receiver.getUserId(), newNotificationSentBySocketDto);
    }
}
