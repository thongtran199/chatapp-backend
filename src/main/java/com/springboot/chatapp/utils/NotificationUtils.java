package com.springboot.chatapp.utils;

import com.springboot.chatapp.model.entity.Notification;
import com.springboot.chatapp.model.entity.User;
import com.springboot.chatapp.model.enums.NotificationType;
import com.springboot.chatapp.model.dto.socket.NewNotificationSentBySocketDto;

public class NotificationUtils {

    public static NewNotificationSentBySocketDto createNotificationSocketDTO(User user, Notification notification, String content) {
        NewNotificationSentBySocketDto newNotificationSentBySocketDto = new NewNotificationSentBySocketDto();

        newNotificationSentBySocketDto.setAvatarUrl(user.getAvatarUrl());

        newNotificationSentBySocketDto.setType(notification.getType());
        newNotificationSentBySocketDto.setReferenceId(notification.getReferenceId());
        newNotificationSentBySocketDto.setContent(content);
        newNotificationSentBySocketDto.setNotificationId(notification.getNotificationId());
        newNotificationSentBySocketDto.setSeen(notification.isSeen());
        newNotificationSentBySocketDto.setCreatedAt(notification.getCreatedAt());
        newNotificationSentBySocketDto.setPartnerId(user.getUserId());

        NotificationType notificationType = notification.getType();

        if (notificationType == NotificationType.FRIEND_REQUEST_RECEIVED) {
            newNotificationSentBySocketDto.setHeader("Lời mời kết bạn mới");
        } else if (notificationType == NotificationType.FRIEND_REQUEST_ACCEPTED) {
            newNotificationSentBySocketDto.setHeader("Lời mời kết bạn đã được chấp nhận");
        } else if (notificationType == NotificationType.MESSAGE) {
            newNotificationSentBySocketDto.setHeader("Tin nhắn mới từ " + user.getFullName());
        }

        return newNotificationSentBySocketDto;
    }
}
