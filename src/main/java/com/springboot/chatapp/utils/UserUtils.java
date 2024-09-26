package com.springboot.chatapp.utils;

import com.springboot.chatapp.domain.dto.user.response.UserProfileDTO;
import com.springboot.chatapp.domain.entity.User;
import org.modelmapper.ModelMapper;

public class UserUtils {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static UserProfileDTO mapToUserProfile(User user) {
        return modelMapper.map(user, UserProfileDTO.class);
    }
}
