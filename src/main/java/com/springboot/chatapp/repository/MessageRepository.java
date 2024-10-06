package com.springboot.chatapp.repository;

import com.springboot.chatapp.model.entity.Message;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE (m.messageSender.userId = :userId1 AND m.messageReceiver.userId = :userId2) OR (m.messageSender.userId = :userId2 AND m.messageReceiver.userId = :userId1)")
    List<Message> findByMessageSenderAndReceiver(Long userId1, Long userId2);

    @Transactional
    @Modifying
    @Query("UPDATE Message m SET m.isRead = true WHERE m.messageId = :messageId")
    void markMessageAsRead(@Param("messageId") Long messageId);

    @Query("SELECT m FROM Message m WHERE m.messageId IN (" +
            "SELECT MAX(m1.messageId) FROM Message m1 WHERE m1.messageSender.id = :userId " +
            "GROUP BY m1.messageReceiver.id " +
            "UNION " +
            "SELECT MAX(m2.messageId) FROM Message m2 WHERE m2.messageReceiver.id = :userId " +
            "GROUP BY m2.messageSender.id)")
    List<Message> findLatestMessagesByUserId(@Param("userId") Long userId);


}
