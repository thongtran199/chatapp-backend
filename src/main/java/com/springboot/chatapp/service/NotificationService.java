package com.springboot.chatapp.service;

import com.springboot.chatapp.payload.notification.NewNotificationDTO;
import com.springboot.chatapp.domain.entity.Notification;

import java.util.List;

public interface NotificationService {
    Notification save(NewNotificationDTO newNotificationDTO);

    Notification findById(Long notificationId);

    List<Notification> findByUserId(Long userId);

    void markNotificationAsRead(Long notificationId);

    void deleteNotification(Long notificationId);

    boolean existsById(Long notificationId);
}

