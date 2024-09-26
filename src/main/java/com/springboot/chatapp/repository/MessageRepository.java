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


}
