package com.springboot.chatapp.service.impl;

import com.springboot.chatapp.payload.notification.NewNotificationDTO;
import com.springboot.chatapp.domain.entity.Notification;
import com.springboot.chatapp.domain.entity.User;
import com.springboot.chatapp.exception.ResourceNotFoundException;
import com.springboot.chatapp.repository.NotificationRepository;
import com.springboot.chatapp.service.NotificationService;
import com.springboot.chatapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;

    @Override
    public Notification save(NewNotificationDTO newNotificationDTO) {
        Notification notification = new Notification();

        User user = userService.findById(newNotificationDTO.getUserId());
        notification.setUser(user);

        notification.setType(newNotificationDTO.getType());
        notification.setReferenceId(newNotificationDTO.getReferenceId());
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
