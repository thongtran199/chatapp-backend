package com.springboot.chatapp.service.impl;

import com.springboot.chatapp.model.dto.notification.NotificationRequestDto;
import com.springboot.chatapp.model.entity.Notification;
import com.springboot.chatapp.model.entity.User;
import com.springboot.chatapp.model.exception.ResourceNotFoundException;
import com.springboot.chatapp.repository.NotificationRepository;
import com.springboot.chatapp.service.NotificationService;
import com.springboot.chatapp.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;

    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            UserService userService) {

        this.notificationRepository = notificationRepository;
        this.userService = userService;
    }

    @Override
    public Notification save(NotificationRequestDto notificationRequestDTO) {
        Notification notification = new Notification();

        User user = userService.findById(notificationRequestDTO.getUserId());
        notification.setUser(user);

        notification.setType(notificationRequestDTO.getType());
        notification.setReferenceId(notificationRequestDTO.getReferenceId());
        return notificationRepository.save(notification);
    }

    @Override
    public Notification findById(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "notificationId", notificationId));
    }

    @Override
    public List<Notification> findByUserId(Long userId) {
        return notificationRepository.findByUser_UserId(userId);
    }

    @Override
    public void markNotificationAsRead(Long notificationId) {
        Notification notification = findById(notificationId);
        notification.setSeen(true);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new ResourceNotFoundException("Notification", "notificationId", notificationId);
        }
        notificationRepository.deleteNotification(notificationId);
    }

    @Override
    public boolean existsById(Long notificationId) {
        return notificationRepository.existsById(notificationId);
    }
}
