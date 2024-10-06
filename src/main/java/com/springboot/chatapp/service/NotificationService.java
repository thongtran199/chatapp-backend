package com.springboot.chatapp.service;

import com.springboot.chatapp.model.dto.notification.NotificationRequestDto;
import com.springboot.chatapp.model.entity.Notification;

import java.util.List;

public interface NotificationService {
    Notification save(NotificationRequestDto notificationRequestDTO);

    Notification findById(Long notificationId);

    List<Notification> findByUserId(Long userId);

    void markNotificationAsRead(Long notificationId);

    void deleteNotification(Long notificationId);

    boolean existsById(Long notificationId);
}

