package com.springboot.chatapp;

import com.springboot.chatapp.model.entity.*;
import com.springboot.chatapp.model.enums.FriendshipStatus;
import com.springboot.chatapp.model.enums.NotificationType;
import org.aspectj.weaver.ast.Not;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.N;

import java.time.LocalDateTime;

public class TestDataUtils {
    public static User createUser(String username, String email, String fullName) {
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash("abc");
        user.setFullName(fullName);
        user.setEmail(email);
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
