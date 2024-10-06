package com.springboot.chatapp.service.manager.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.chatapp.model.dto.friendship.FriendshipRequestDto;
import com.springboot.chatapp.model.dto.user.SearchedUserResponseDto;
import com.springboot.chatapp.model.entity.Friendship;
import com.springboot.chatapp.model.entity.Notification;
import com.springboot.chatapp.model.entity.User;
import com.springboot.chatapp.model.enums.FriendshipStatus;
import com.springboot.chatapp.model.enums.NotificationType;
import com.springboot.chatapp.service.manager.FriendshipManager;
import com.springboot.chatapp.service.SocketService;
import com.springboot.chatapp.service.manager.factory.FriendshipNotificationFactory;
import com.springboot.chatapp.model.dto.socket.NewNotificationSentBySocketDto;
import com.springboot.chatapp.service.FriendshipService;
import com.springboot.chatapp.service.UserService;
import com.springboot.chatapp.utils.NotificationUtils;
import com.springboot.chatapp.utils.UserUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FriendshipManagerImpl implements FriendshipManager {
    private final FriendshipService friendshipService;
    private final UserService userService;
    private final SocketService socketService;
    private final FriendshipNotificationFactory friendshipNotificationFactory;

    public FriendshipManagerImpl(
            FriendshipService friendshipService,
            UserService userService,
            SocketService socketService,
            FriendshipNotificationFactory friendshipNotificationFactory) {

        this.friendshipService = friendshipService;
        this.userService = userService;
        this.socketService = socketService;
        this.friendshipNotificationFactory = friendshipNotificationFactory;
    }

    @Override
    @Transactional
    public void sendFriendRequestAndNotification(FriendshipRequestDto friendshipRequestDTO) throws JsonProcessingException {
        Optional<Friendship> friendshipOptional = friendshipService.findLatestFriendshipBetweenUsers(friendshipRequestDTO.getRequesterId(), friendshipRequestDTO.getRequestedUserId());
        if(friendshipOptional.isPresent() && ( friendshipOptional.get().getStatus().equals(FriendshipStatus.PENDING) || friendshipOptional.get().getStatus().equals(FriendshipStatus.ACCEPTED)))
        {
            throw new AlreadyHaveFriendshipIsPendingException(friendshipRequestDTO);
        }
        Friendship friendship = friendshipService.sendFriendRequest(friendshipRequestDTO);
        User requestedUser = friendship.getRequestedUser();
        Notification notification = friendshipNotificationFactory.createNotificationHandler(NotificationType.FRIEND_REQUEST_RECEIVED).save(friendship);
        User requester = userService.findById(friendshipRequestDTO.getRequesterId());

        NewNotificationSentBySocketDto newNotificationSentBySocketDto = NotificationUtils.createNotificationSocketDTO(requester, notification, requester.getFullName());
        socketService.sendNotificationToUser(requestedUser.getUserId(), newNotificationSentBySocketDto);
    }

    @Override
    @Transactional
    public void acceptFriendRequestAndNotification(Long friendshipId) throws JsonProcessingException {
        friendshipService.acceptFriendRequest(friendshipId);
        Friendship friendship = friendshipService.findById(friendshipId);
        User requester = friendship.getRequester();
        Notification notification = friendshipNotificationFactory.createNotificationHandler(NotificationType.FRIEND_REQUEST_ACCEPTED).save(friendship);
        User requestedUser = userService.findById(friendship.getRequestedUser().getUserId());

        NewNotificationSentBySocketDto newNotificationSentBySocketDto = NotificationUtils.createNotificationSocketDTO(requestedUser, notification, requestedUser.getFullName());
        socketService.sendNotificationToUser(requester.getUserId(), newNotificationSentBySocketDto);
    }

    @Override
    public List<SearchedUserResponseDto> findAcceptedFriendshipsByUserId(Long userId) {
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
            return UserUtils.getSearchedUserResponseDTOByUserAndFriendship(user, Optional.of(friendship));
        }).collect(Collectors.toList());
    }

}
