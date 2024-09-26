package com.springboot.chatapp.mapper.impl;

import com.springboot.chatapp.domain.dto.user.response.FriendshipResponseDTO;
import com.springboot.chatapp.domain.entity.Friendship;
import com.springboot.chatapp.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class FriendshipMapper implements Mapper<Friendship, FriendshipResponseDTO> {
    private ModelMapper modelMapper;
    public FriendshipMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;

    }
    @Override
    public FriendshipResponseDTO mapToResponseDTO(Friendship friendship) {
        return modelMapper.map(friendship, FriendshipResponseDTO.class);
    }

    @Override
    public Friendship mapToEntity(FriendshipResponseDTO friendshipResponseDto) {
        return modelMapper.map(friendshipResponseDto, Friendship.class);
    }
}