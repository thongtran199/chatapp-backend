package com.springboot.chatapp.controller;

import com.springboot.chatapp.model.dto.notification.NotificationResponseDto;
import com.springboot.chatapp.model.entity.Notification;
import com.springboot.chatapp.utils.mapper.impl.NotificationMapper;
import com.springboot.chatapp.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    public NotificationController(
            NotificationService notificationService,
            NotificationMapper notificationMapper) {

        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponseDto> getNotificationById(@PathVariable Long notificationId) {
        Notification notification = notificationService.findById(notificationId);
        NotificationResponseDto notificationResponseDTO = notificationMapper.mapToResponseDTO(notification);
        return ResponseEntity.ok(notificationResponseDTO);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDto>> getNotificationsByUserId(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.findByUserId(userId);
        List<NotificationResponseDto> notificationResponseDtos = notifications.stream()
                .map(notificationMapper::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(notificationResponseDtos);
    }

    @PostMapping("/mark-read/{notificationId}")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
