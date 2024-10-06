package com.springboot.chatapp.utils;

import com.springboot.chatapp.model.dto.user.SearchedUserResponseDto;
import com.springboot.chatapp.model.dto.user.UserProfileResponseDto;
import com.springboot.chatapp.model.entity.Friendship;
import com.springboot.chatapp.model.entity.User;
import org.modelmapper.ModelMapper;

import java.util.Optional;

public class UserUtils {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static UserProfileResponseDto mapToUserProfile(User user) {
        return modelMapper.map(user, UserProfileResponseDto.class);
    }

    public static SearchedUserResponseDto getSearchedUserResponseDTOByUserAndFriendship(User user,
                                                                                        Optional<Friendship> latestFriendship) {
        SearchedUserResponseDto dto = new SearchedUserResponseDto();
        dto.setUserId(user.getUserId());
        dto.setFullName(user.getFullName());
        dto.setUsername(user.getUsername());
        dto.setAvatarUrl(user.getAvatarUrl());
        if(latestFriendship.isEmpty())
        {
            dto.setFriendshipSearchedUserResponseDto(null);
            return dto;
        }
        Friendship friendship = latestFriendship.get();
        SearchedUserResponseDto.FriendshipSearchedUserResponseDto friendshipSearchedUserResponseDto = dto.new FriendshipSearchedUserResponseDto();

        friendshipSearchedUserResponseDto.setFriendshipId(friendship.getFriendshipId());
        friendshipSearchedUserResponseDto.setFriendshipStatus(friendship.getStatus());
        friendshipSearchedUserResponseDto.setRequesterId(friendship.getRequester().getUserId());
        friendshipSearchedUserResponseDto.setRequestedUserId(friendship.getRequestedUser().getUserId());

        dto.setFriendshipSearchedUserResponseDto(friendshipSearchedUserResponseDto);

        return dto;
    }
}
