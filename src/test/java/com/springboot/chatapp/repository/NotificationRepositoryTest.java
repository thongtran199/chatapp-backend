package com.springboot.chatapp.repository;

import com.springboot.chatapp.TestDataUtils;
import com.springboot.chatapp.model.entity.Notification;
import com.springboot.chatapp.model.entity.User;
import com.springboot.chatapp.model.enums.NotificationType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import jakarta.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NotificationRepositoryTest {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserRepository userRepository;

    private User user;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        user = TestDataUtils.createUser("thongtran", "thongtran@gmail.com", "Tran Van Thong");
        userRepository.save(user);

        Notification notification1 = TestDataUtils.createNotification(user, NotificationType.MESSAGE, 1L);
        Notification notification2 = TestDataUtils.createNotification(user, NotificationType.FRIEND_REQUEST_RECEIVED, 2L);

        notificationRepository.save(notification1);
        notificationRepository.save(notification2);
    }

    @AfterEach
    void tearDown() {
        notificationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByUser_UserId() {
        List<Notification> notifications = notificationRepository.findByUser_UserId(user.getUserId());
        assertThat(notifications).hasSize(2);
        assertThat(notifications.get(0).getUser().getUserId()).isEqualTo(user.getUserId());
    }

    @Test
    void markNotificationAsRead() {
        List<Notification> notifications = notificationRepository.findByUser_UserId(user.getUserId());
        Long notificationId = notifications.get(0).getNotificationId();

        notificationRepository.markNotificationAsRead(notificationId);

        entityManager.flush();
        entityManager.clear();

        Notification updatedNotification = notificationRepository.findById(notificationId).orElseThrow();
        assertThat(updatedNotification.isSeen()).isTrue();
    }

    @Test
    void deleteNotification() {
        List<Notification> notifications = notificationRepository.findByUser_UserId(user.getUserId());
        Long notificationId = notifications.get(0).getNotificationId();

        notificationRepository.deleteNotification(notificationId);

        List<Notification> remainingNotifications = notificationRepository.findByUser_UserId(user.getUserId());
        assertThat(remainingNotifications).hasSize(1);
    }
}