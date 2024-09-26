package com.springboot.chatapp.mapper.impl;

import com.springboot.chatapp.domain.dto.user.response.UserResponseDTO;
import com.springboot.chatapp.domain.entity.User;
import com.springboot.chatapp.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<User, UserResponseDTO> {
    private ModelMapper modelMapper;
    public UserMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;

    }
    @Override
    public UserResponseDTO mapToResponseDTO(User user) {
        return modelMapper.map(user, UserResponseDTO.class);
    }

    @Override
    public User mapToEntity(UserResponseDTO userDto) {
        return modelMapper.map(userDto, User.class);
    }
}