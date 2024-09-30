package com.springboot.chatapp.manager.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.chatapp.domain.dto.user.request.FriendshipRequestDTO;
import com.springboot.chatapp.domain.dto.user.response.FoundUserResponseDTO;
import com.springboot.chatapp.domain.entity.Friendship;
import com.springboot.chatapp.domain.entity.Notification;
import com.springboot.chatapp.domain.entity.User;
import com.springboot.chatapp.domain.enumerate.FriendshipStatus;
import com.springboot.chatapp.domain.enumerate.NotificationType;
import com.springboot.chatapp.exception.AlreadyHaveFriendshipIsPendingException;
import com.springboot.chatapp.manager.FriendshipManager;
import com.springboot.chatapp.manager.SocketManager;
import com.springboot.chatapp.manager.factory.FriendshipNotificationFactory;
import com.springboot.chatapp.mapper.impl.NotificationMapper;
import com.springboot.chatapp.payload.notification.NewNotificationSocketDTO;
import com.springboot.chatapp.service.FriendshipService;
import com.springboot.chatapp.service.UserService;
import com.springboot.chatapp.utils.NotificationUtils;
import com.springboot.chatapp.utils.UserUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public void sendFriendRequestAndNotification(FriendshipRequestDTO friendshipRequestDTO) throws JsonProcessingException {
        Optional<Friendship> friendshipOptional = friendshipService.findLatestFriendshipBetweenUsers(friendshipRequestDTO.getRequesterId(), friendshipRequestDTO.getRequestedUserId());
        if(friendshipOptional.isPresent() && ( friendshipOptional.get().getStatus().equals(FriendshipStatus.PENDING) || friendshipOptional.get().getStatus().equals(FriendshipStatus.ACCEPTED)))
        {
            throw new AlreadyHaveFriendshipIsPendingException(friendshipRequestDTO);
        }
        Friendship friendship = friendshipService.sendFriendRequest(friendshipRequestDTO);
        User requestedUser = friendship.getRequestedUser();
        Notification notification = friendshipNotificationFactory.createNotificationHandler(NotificationType.FRIEND_REQUEST_RECEIVED).save(friendship);
        User requester = userService.findById(friendshipRequestDTO.getRequesterId());

        NewNotificationSocketDTO newNotificationSocketDTO = NotificationUtils.createNotificationSocketDTO(requester, notification, requester.getFullName());
        socketManager.sendNotificationToUser(requestedUser.getUserId(), newNotificationSocketDTO);
    }

    @Override
    @Transactional
    public void acceptFriendRequestAndNotification(Long friendshipId) throws JsonProcessingException {
        friendshipService.acceptFriendRequest(friendshipId);
        Friendship friendship = friendshipService.findById(friendshipId);
        User requester = friendship.getRequester();
        Notification notification = friendshipNotificationFactory.createNotificationHandler(NotificationType.FRIEND_REQUEST_ACCEPTED).save(friendship);
        User requestedUser = userService.findById(friendship.getRequestedUser().getUserId());

        NewNotificationSocketDTO newNotificationSocketDTO = NotificationUtils.createNotificationSocketDTO(requestedUser, notification, requestedUser.getFullName());
        socketManager.sendNotificationToUser(requester.getUserId(), newNotificationSocketDTO);
    }

    @Override
    public List<FoundUserResponseDTO> findAcceptedFriendshipsByUserId(Long userId) {
        List<Friendship> friendships = friendshipService.findAcceptedFriendshipsByUserId(userId);
        return friendships.stream().map(friendship -> {
            User user;
            if(friendship.getRequester().getUserId().equals(userId))
            {
                user = friendship.getRequestedUser();
            }
            else{
                user = friendship.getRequester();
            }
            return UserUtils.getFoundUserResponseDTOByUserAndFriendship(user, Optional.of(friendship));
        }).collect(Collectors.toList());
    }

}
