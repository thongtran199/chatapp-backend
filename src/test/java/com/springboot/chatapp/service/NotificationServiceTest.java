package com.springboot.chatapp.service;

import com.springboot.chatapp.model.dto.notification.NotificationRequestDto;
import com.springboot.chatapp.model.entity.Notification;
import com.springboot.chatapp.model.entity.User;
import com.springboot.chatapp.model.enums.NotificationType;
import com.springboot.chatapp.model.exception.ResourceNotFoundException;
import com.springboot.chatapp.repository.NotificationRepository;
import com.springboot.chatapp.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save() {
        NotificationRequestDto notificationRequest = new NotificationRequestDto();
        notificationRequest.setUserId(1L);
        notificationRequest.setType(NotificationType.FRIEND_REQUEST_RECEIVED);
        notificationRequest.setReferenceId(10L);

        User user = new User();
        user.setUserId(1L);

        when(userService.findById(1L)).thenReturn(user);
        when(notificationRepository.save(any(Notification.class))).thenReturn(new Notification());

        Notification result = notificationService.save(notificationRequest);

        assertNotNull(result);
        verify(userService, times(1)).findById(1L);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void findById() {
        Notification notification = new Notification();
        notification.setNotificationId(1L);

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        Notification result = notificationService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getNotificationId());
        verify(notificationRepository, times(1)).findById(1L);
    }

    @Test
    void findById_NotFound() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> notificationService.findById(1L));
        verify(notificationRepository, times(1)).findById(1L);
    }

    @Test
    void findByUserId() {
        when(notificationRepository.findByUser_UserId(1L)).thenReturn(List.of(new Notification()));

        List<Notification> result = notificationService.findByUserId(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(notificationRepository, times(1)).findByUser_UserId(1L);
    }

    @Test
    void markNotificationAsRead() {
        Notification notification = new Notification();
        notification.setNotificationId(1L);
        notification.setSeen(false);

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        notificationService.markNotificationAsRead(1L);

        assertTrue(notification.isSeen());
        verify(notificationRepository, times(1)).findById(1L);
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void deleteNotification() {
        when(notificationRepository.existsById(1L)).thenReturn(true);

        notificationService.deleteNotification(1L);

        verify(notificationRepository, times(1)).deleteNotification(1L);
    }

    @Test
    void deleteNotification_NotFound() {
        when(notificationRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> notificationService.deleteNotification(1L));
        verify(notificationRepository, times(1)).existsById(1L);
    }

    @Test
    void existsById() {
        when(notificationRepository.existsById(1L)).thenReturn(true);

        boolean result = notificationService.existsById(1L);

        assertTrue(result);
        verify(notificationRepository, times(1)).existsById(1L);
    }
}
