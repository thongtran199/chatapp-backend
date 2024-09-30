package com.springboot.chatapp.utils;

import com.springboot.chatapp.domain.entity.Notification;
import com.springboot.chatapp.domain.entity.User;
import com.springboot.chatapp.domain.enumerate.NotificationType;
import com.springboot.chatapp.payload.notification.NewNotificationSocketDTO;

public class NotificationUtils {

    public static NewNotificationSocketDTO createNotificationSocketDTO(User user, Notification notification, String content) {
        NewNotificationSocketDTO newNotificationSocketDTO = new NewNotificationSocketDTO();

        newNotificationSocketDTO.setAvatarUrl(user.getAvatarUrl());

        newNotificationSocketDTO.setType(notification.getType());
        newNotificationSocketDTO.setReferenceId(notification.getReferenceId());
        newNotificationSocketDTO.setContent(content);
        newNotificationSocketDTO.setNotificationId(notification.getNotificationId());
        newNotificationSocketDTO.setSeen(notification.isSeen());
        newNotificationSocketDTO.setCreatedAt(notification.getCreatedAt());
        newNotificationSocketDTO.setPartnerId(user.getUserId());

        NotificationType notificationType = notification.getType();
        if (notificationType == NotificationType.FRIEND_REQUEST_RECEIVED) {
            newNotificationSocketDTO.setHeader("Lời mời kết bạn mới");
        } else if (notificationType == NotificationType.FRIEND_REQUEST_ACCEPTED) {
            newNotificationSocketDTO.setHeader("Lời mời kết bạn đã được chấp nhận");
        } else if (notificationType == NotificationType.MESSAGE) {
            newNotificationSocketDTO.setHeader("Tin nhắn mới từ " + user.getFullName());
        }

        return newNotificationSocketDTO;
    }
}
