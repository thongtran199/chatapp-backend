package com.springboot.chatapp.service.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.chatapp.model.dto.friendship.FriendshipRequestDto;
import com.springboot.chatapp.model.dto.user.SearchedUserResponseDto;
import com.springboot.chatapp.model.entity.Friendship;

import java.util.List;

public interface FriendshipManager {
        Friendship sendFriendRequestAndNotification(FriendshipRequestDto friendshipRequestDTO) throws JsonProcessingException;
        void acceptFriendRequestAndNotification(Long friendshipId) throws JsonProcessingException;
        List<SearchedUserResponseDto> findAcceptedFriendshipsByUserId(Long userId);
}
