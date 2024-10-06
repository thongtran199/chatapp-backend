package com.springboot.chatapp.utils.mapper.impl;

import com.springboot.chatapp.model.dto.notification.NotificationResponseDto;
import com.springboot.chatapp.model.entity.Notification;
import com.springboot.chatapp.utils.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper implements Mapper<Notification, NotificationResponseDto> {
    private ModelMapper modelMapper;

    public NotificationMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;

    }
    @Override
    public NotificationResponseDto mapToResponseDTO(Notification notification) {
        return modelMapper.map(notification, NotificationResponseDto.class);
    }

    @Override
    public Notification mapToEntity(NotificationResponseDto notificationResponseDto) {
        return modelMapper.map(notificationResponseDto, Notification.class);
    }
}