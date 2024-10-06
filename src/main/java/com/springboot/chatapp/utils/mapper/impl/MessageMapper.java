package com.springboot.chatapp.utils.mapper.impl;

import com.springboot.chatapp.model.dto.message.MessageResponseDto;
import com.springboot.chatapp.model.entity.Message;
import com.springboot.chatapp.utils.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper implements Mapper<Message, MessageResponseDto> {
    private ModelMapper modelMapper;
    public MessageMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;

    }
    @Override
    public MessageResponseDto mapToResponseDTO(Message message) {
        return modelMapper.map(message, MessageResponseDto.class);
    }

    @Override
    public Message mapToEntity(MessageResponseDto messageResponseDto) {
        return modelMapper.map(messageResponseDto, Message.class);
    }
}