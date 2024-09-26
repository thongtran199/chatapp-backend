package com.springboot.chatapp.manager.impl;

import com.springboot.chatapp.domain.dto.user.request.FriendshipRequestDTO;
import com.springboot.chatapp.domain.entity.Friendship;
import com.springboot.chatapp.domain.entity.Notification;
import com.springboot.chatapp.domain.entity.User;
import com.springboot.chatapp.domain.enumerate.NotificationType;
import com.springboot.chatapp.manager.FriendshipManager;
import com.springboot.chatapp.manager.SocketManager;
import com.springboot.chatapp.manager.factory.FriendshipNotificationFactory;
import com.springboot.chatapp.mapper.impl.NotificationMapper;
import com.springboot.chatapp.payload.notification.NewNotificationSocketDTO;
import com.springboot.chatapp.service.FriendshipService;
import com.springboot.chatapp.service.UserService;
import com.springboot.chatapp.utils.NotificationUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FriendshipManagerImpl implements FriendshipManager {
    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private UserService userService;


    @Autowired
    private SocketManager socketManager;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private FriendshipNotificationFactory friendshipNotificationFactory;


    @Override
    @Transactional
    public void saveFriendRequestAndNotification(FriendshipRequestDTO friendshipRequestDTO) {
        Friendship friendship = friendshipService.save(friendshipRequestDTO);
        Notification notification = friendshipNotificationFactory.createNotificationHandler(NotificationType.FRIEND_REQUEST_RECEIVED).save(friendship);
        User requester = userService.findById(friendshipRequestDTO.getRequesterId());

        NewNotificationSocketDTO newNotificationSocketDTO = NotificationUtils.createNotificationSocketDTO(requester, notification, requester.getFullName());
        socketManager.sendNotificationToUser(friendshipRequestDTO.getRequestedUserId(), newNotificationSocketDTO);
    }

    @Override
    @Transactional
    public void acceptFriendRequestAndNotification(Long requesterId, Long requestedUserId) {
        friendshipService.acceptFriendRequest(requesterId, requestedUserId);
        Friendship friendship = friendshipService.findAcceptedFriendshipBetweenUsers(requesterId, requestedUserId);
        Notification notification = friendshipNotificationFactory.createNotificationHandler(NotificationType.FRIEND_REQUEST_ACCEPTED).save(friendship);
        User requestedUser = userService.findById(requestedUserId);

        NewNotificationSocketDTO newNotificationSocketDTO = NotificationUtils.createNotificationSocketDTO(requestedUser, notification, requestedUser.getFullName());
        socketManager.sendNotificationToUser(requesterId, newNotificationSocketDTO);


    }

    @Override
    public List<User> getAllFriends(Long userId) {
        List<Friendship> friendships = friendshipService.findAllFriendsByUserId(userId);
        List<User> friends = new ArrayList<>();
        for (Friendship friendship : friendships) {
            if (friendship.getRequester().getUserId().equals(userId)) {
                friends.add(friendship.getRequestedUser());
            } else if (friendship.getRequestedUser().getUserId().equals(userId)) {
                friends.add(friendship.getRequester());
            }
        }
        return friends;
    }
}
