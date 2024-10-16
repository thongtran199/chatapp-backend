package com.springboot.chatapp;

import com.springboot.chatapp.model.entity.*;
import com.springboot.chatapp.model.enums.FriendshipStatus;
import com.springboot.chatapp.model.enums.NotificationType;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.N;

import java.time.LocalDateTime;

@Component
public class TestDataUtils {
    public static User createUser(String username, String email, String fullName) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringBootChatAppRestApiApplication.class);
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        User user = new User();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode("Matkhaunayratmanh123@"));
        return user;
    }

    public static Notification createNotification(User user, NotificationType type, Long referenceId) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setReferenceId(referenceId);
        return notification;
    }


    public static Message createMessage(User sender, User receiver, String content) {
        Message message = new Message();
        message.setMessageSender(sender);
        message.setMessageReceiver(receiver);
        message.setContent(content);
        return message;
    }

    public static Role createRole(String name) {
        Role role = new Role();
        role.setName(name);
        return role;
    }

    public static PasswordResetToken passwordResetToken(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUser(user);
        passwordResetToken.setResetToken(token);
        passwordResetToken.setExpiresAt(LocalDateTime.now().plusHours(1));
        return passwordResetToken;
    }

    public static Friendship createFriendship(User requester, User requestedUser, FriendshipStatus status) {
        Friendship friendship = new Friendship();
        friendship.setRequester(requester);
        friendship.setRequestedUser(requestedUser);
        friendship.setStatus(status);
        return friendship;
    }
}
