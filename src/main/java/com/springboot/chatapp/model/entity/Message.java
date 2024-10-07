package com.springboot.chatapp.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User messageSender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User messageReceiver;

    @Column(nullable = false)
    private String content;

    private LocalDateTime sentAt = LocalDateTime.now();

    @Column(nullable = false)
    private boolean isRead = false;
}