package com.springboot.chatapp.utils;

import com.springboot.chatapp.domain.dto.user.response.FoundUserResponseDTO;
import com.springboot.chatapp.domain.dto.user.response.FriendshipFoundUserResponseDTO;
import com.springboot.chatapp.domain.dto.user.response.UserProfileDTO;
import com.springboot.chatapp.domain.entity.Friendship;
import com.springboot.chatapp.domain.entity.User;
import com.springboot.chatapp.service.FriendshipService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

public class UserUtils {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static UserProfileDTO mapToUserProfile(User user) {
        return modelMapper.map(user, UserProfileDTO.class);
    }

    public static FoundUserResponseDTO getFoundUserResponseDTOByUserAndFriendship(User user, Optional<Friendship> latestFriendship) {
        FoundUserResponseDTO dto = new FoundUserResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setFullName(user.getFullName());
        dto.setUsername(user.getUsername());
        dto.setAvatarUrl(user.getAvatarUrl());
        if (!latestFriendship.isEmpty()) {
            FriendshipFoundUserResponseDTO friendshipFoundUserResponseDTO = FriendshipUtils.getFriendshipFoundUserResponseDTO(latestFriendship.get());
            dto.setFriendshipFoundUserResponseDTO(friendshipFoundUserResponseDTO);
        } else {
            dto.setFriendshipFoundUserResponseDTO(null);
        }
        return dto;

    }
}
