package com.springboot.chatapp.utils.mapper.impl;

import com.springboot.chatapp.model.dto.user.UserResponseDto;
import com.springboot.chatapp.model.entity.User;
import com.springboot.chatapp.utils.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<User, UserResponseDto> {
    private ModelMapper modelMapper;
    public UserMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;

    }
    @Override
    public UserResponseDto mapToResponseDTO(User user) {
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public User mapToEntity(UserResponseDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}