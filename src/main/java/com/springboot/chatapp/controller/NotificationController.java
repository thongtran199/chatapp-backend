package com.springboot.chatapp.controller;

import com.springboot.chatapp.domain.dto.user.response.NotificationResponseDTO;
import com.springboot.chatapp.domain.entity.Notification;
import com.springboot.chatapp.mapper.impl.NotificationMapper;
import com.springboot.chatapp.service.NotificationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationMapper notificationMapper;

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponseDTO> getNotificationById(@PathVariable Long notificationId) {
        Notification notification = notificationService.findById(notificationId);
        NotificationResponseDTO notificationResponseDTO = notificationMapper.mapToResponseDTO(notification);
        return ResponseEntity.ok(notificationResponseDTO);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationsByUserId(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.findByUserId(userId);
        List<NotificationResponseDTO> notificationResponseDTOs = notifications.stream()
                .map(notificationMapper::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(notificationResponseDTOs);
    }

    @PostMapping("/mark-read/{notificationId}")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }
}
