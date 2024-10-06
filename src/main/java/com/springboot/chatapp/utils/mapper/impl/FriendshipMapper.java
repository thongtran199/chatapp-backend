package com.springboot.chatapp.utils.mapper.impl;

import com.springboot.chatapp.model.dto.friendship.FriendshipResponseDto;
import com.springboot.chatapp.model.entity.Friendship;
import com.springboot.chatapp.utils.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class FriendshipMapper implements Mapper<Friendship, FriendshipResponseDto> {
    private ModelMapper modelMapper;
    public FriendshipMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;

    }
    @Override
    public FriendshipResponseDto mapToResponseDTO(Friendship friendship) {
        return modelMapper.map(friendship, FriendshipResponseDto.class);
    }

    @Override
    public Friendship mapToEntity(FriendshipResponseDto friendshipResponseDto) {
        return modelMapper.map(friendshipResponseDto, Friendship.class);
    }
}