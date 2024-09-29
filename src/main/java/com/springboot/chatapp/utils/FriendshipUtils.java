package com.springboot.chatapp.utils;

import com.springboot.chatapp.domain.dto.user.request.FriendshipRequestDTO;
import com.springboot.chatapp.domain.dto.user.response.FriendshipFoundUserResponseDTO;
import com.springboot.chatapp.domain.dto.user.response.UserProfileDTO;
import com.springboot.chatapp.domain.entity.Friendship;
import com.springboot.chatapp.domain.entity.User;
import org.modelmapper.ModelMapper;

import java.util.Optional;

public class FriendshipUtils {
    public static FriendshipFoundUserResponseDTO getFriendshipFoundUserResponseDTO(Friendship friendship) {

        FriendshipFoundUserResponseDTO friendshipFoundUserResponseDTO = new FriendshipFoundUserResponseDTO();
        friendshipFoundUserResponseDTO.setFriendshipId(friendship.getFriendshipId());
        friendshipFoundUserResponseDTO.setFriendshipStatus(friendship.getStatus());
        friendshipFoundUserResponseDTO.setRequesterId(friendship.getRequester().getUserId());
        friendshipFoundUserResponseDTO.setRequestedUserId(friendship.getRequestedUser().getUserId());
        return friendshipFoundUserResponseDTO;
    }





}
