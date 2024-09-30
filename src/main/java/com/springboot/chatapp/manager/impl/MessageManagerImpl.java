package com.springboot.chatapp.manager.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.chatapp.domain.dto.user.request.MessageRequestDTO;
import com.springboot.chatapp.domain.entity.Message;
import com.springboot.chatapp.domain.entity.Notification;
import com.springboot.chatapp.domain.entity.User;
import com.springboot.chatapp.domain.enumerate.NotificationType;
import com.springboot.chatapp.manager.MessageManager;
import com.springboot.chatapp.manager.SocketManager;
import com.springboot.chatapp.payload.notification.NewNotificationDTO;
import com.springboot.chatapp.payload.notification.NewNotificationSocketDTO;
import com.springboot.chatapp.service.MessageService;
import com.springboot.chatapp.service.NotificationService;
import com.springboot.chatapp.service.UserService;
import com.springboot.chatapp.utils.NotificationUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageManagerImpl implements MessageManager {
    @Autowired
    private MessageService messageService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SocketManager socketManager;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public void saveMessageAndNotification(MessageRequestDTO messageRequestDTO) throws JsonProcessingException {
        Message message = messageService.save(messageRequestDTO);
        User receiver = message.getMessageReceiver();

        NewNotificationDTO newNotificationDTO = new NewNotificationDTO(
                message.getMessageReceiver().getUserId(),
                NotificationType.MESSAGE,
                message.getMessageId()
        );
        Notification notification = notificationService.save(newNotificationDTO);
        User sender = userService.findById(messageRequestDTO.getMessageSenderId());

        NewNotificationSocketDTO newNotificationSocketDTO = NotificationUtils.createNotificationSocketDTO(sender, notification, messageRequestDTO.getContent());
        socketManager.sendMessageToUser(receiver.getUserId(), newNotificationSocketDTO);
    }
}
