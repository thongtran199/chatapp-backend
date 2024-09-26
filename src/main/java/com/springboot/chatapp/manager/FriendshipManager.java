package com.springboot.chatapp.manager;

import com.springboot.chatapp.domain.dto.user.request.FriendshipRequestDTO;
import com.springboot.chatapp.domain.entity.Friendship;
import com.springboot.chatapp.domain.entity.Notification;
import com.springboot.chatapp.domain.entity.User;

import java.util.List;

public interface FriendshipManager {
        void saveFriendRequestAndNotification(FriendshipRequestDTO friendshipRequestDTO);
        void acceptFriendRequestAndNotification(Long requesterId, Long requestedUserId);
        List<User> getAllFriends(Long userId);
}
