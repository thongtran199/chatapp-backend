package com.springboot.chatapp.domain.entity;

import com.springboot.chatapp.domain.enumerate.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private Long referenceId;

    @Column(nullable = false)
    private boolean isSeen = false;

    private LocalDateTime createdAt = LocalDateTime.now();
}