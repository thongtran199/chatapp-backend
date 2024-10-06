package com.springboot.chatapp.controller;

import com.springboot.chatapp.model.dto.notification.NotificationResponseDto;
import com.springboot.chatapp.model.entity.Notification;
import com.springboot.chatapp.utils.mapper.impl.NotificationMapper;
import com.springboot.chatapp.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Notification Management", description = "Endpoints for managing notifications")
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

    @Operation(summary = "Get notification by ID", description = "Retrieve a notification by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Notification retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Notification not found")
            })
    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponseDto> getNotificationById(
            @Parameter(description = "ID of the notification to retrieve", required = true) @PathVariable Long notificationId) {
        Notification notification = notificationService.findById(notificationId);
        NotificationResponseDto notificationResponseDTO = notificationMapper.mapToResponseDTO(notification);
        return ResponseEntity.ok(notificationResponseDTO);
    }

    @Operation(summary = "Get notifications by user ID", description = "Retrieve all notifications associated with a specific user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "No notifications found for this user")
            })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDto>> getNotificationsByUserId(
            @Parameter(description = "ID of the user to retrieve notifications for", required = true) @PathVariable Long userId) {
        List<Notification> notifications = notificationService.findByUserId(userId);
        List<NotificationResponseDto> notificationResponseDtos = notifications.stream()
                .map(notificationMapper::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(notificationResponseDtos);
    }

    @Operation(summary = "Mark a notification as read", description = "Marks the specified notification as read",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Notification marked as read"),
                    @ApiResponse(responseCode = "404", description = "Notification not found")
            })
    @PostMapping("/mark-read/{notificationId}")
    public ResponseEntity<Void> markNotificationAsRead(
            @Parameter(description = "ID of the notification to mark as read", required = true) @PathVariable Long notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Delete a notification", description = "Deletes a notification by its ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Notification deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Notification not found")
            })
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @Parameter(description = "ID of the notification to delete", required = true) @PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
