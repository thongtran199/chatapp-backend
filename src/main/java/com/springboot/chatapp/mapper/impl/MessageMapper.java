package com.springboot.chatapp.mapper.impl;

import com.springboot.chatapp.domain.dto.user.response.MessageResponseDTO;
import com.springboot.chatapp.domain.entity.Message;
import com.springboot.chatapp.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper implements Mapper<Message, MessageResponseDTO> {
    private ModelMapper modelMapper;
    public MessageMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;

    }
    @Override
    public MessageResponseDTO mapToResponseDTO(Message message) {
        return modelMapper.map(message, MessageResponseDTO.class);
    }

    @Override
    public Message mapToEntity(MessageResponseDTO messageResponseDto) {
        return modelMapper.map(messageResponseDto, Message.class);
    }
}