package com.springboot.chatapp.repository;

import com.springboot.chatapp.domain.entity.Message;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE (m.messageSender.userId = :messageSenderId AND m.messageReceiver.userId = :messageReceiverId) OR (m.messageSender.userId = :messageReceiverId AND m.messageReceiver.userId = :messageSenderId)")
    List<Message> findByMessageSenderAndReceiver(Long messageSenderId, Long messageReceiverId);

    @Transactional
    @Modifying
    @Query("UPDATE Message m SET m.isRead = true WHERE m.messageId = :messageId")
    void markMessageAsRead(@Param("messageId") Long messageId);

    @Query("SELECT CASE WHEN m.messageSender.userId = :userId THEN m.messageReceiver.userId ELSE m.messageSender.userId END " +
            "FROM Message m " +
            "WHERE m.messageSender.userId = :userId OR m.messageReceiver.userId = :userId " +
            "GROUP BY CASE WHEN m.messageSender.userId = :userId THEN m.messageReceiver.userId ELSE m.messageSender.userId END")
    List<Long> findConversationPartnerIds(@Param("userId") Long userId);

    @Query("SELECT m FROM Message m " +
            "WHERE ((m.messageSender.userId = :userId AND m.messageReceiver.userId = :partnerId) " +
            "OR (m.messageSender.userId = :partnerId AND m.messageReceiver.userId = :userId)) " +
            "ORDER BY m.sentAt DESC")
    Message findLatestMessageBetweenUsers(@Param("userId") Long userId, @Param("partnerId") Long partnerId);


}
