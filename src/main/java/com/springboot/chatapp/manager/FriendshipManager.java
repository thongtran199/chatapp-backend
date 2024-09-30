package com.springboot.chatapp.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.chatapp.domain.dto.user.request.FriendshipRequestDTO;
import com.springboot.chatapp.domain.dto.user.response.FoundUserResponseDTO;
import com.springboot.chatapp.domain.entity.Friendship;
import com.springboot.chatapp.domain.entity.Notification;
import com.springboot.chatapp.domain.entity.User;

import java.util.List;

public interface FriendshipManager {
        void sendFriendRequestAndNotification(FriendshipRequestDTO friendshipRequestDTO) throws JsonProcessingException;
        void acceptFriendRequestAndNotification(Long friendshipId) throws JsonProcessingException;
        List<FoundUserResponseDTO> findAcceptedFriendshipsByUserId(Long userId);
}
