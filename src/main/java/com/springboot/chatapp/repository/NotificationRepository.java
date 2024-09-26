package com.springboot.chatapp.repository;

import com.springboot.chatapp.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser_UserId(Long userId);

    @Modifying
    @Query("UPDATE Notification n SET n.isSeen = true WHERE n.notificationId = :notificationId")
    void markNotificationAsRead(@Param("notificationId") Long notificationId);

    @Modifying
    @Query("DELETE FROM Notification n WHERE n.notificationId = :notificationId")
    void deleteNotification(@Param("notificationId") Long notificationId);

}
