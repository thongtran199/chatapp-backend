package com.springboot.chatapp.mapper.impl;

import com.springboot.chatapp.domain.dto.user.response.NotificationResponseDTO;
import com.springboot.chatapp.domain.entity.Friendship;
import com.springboot.chatapp.domain.entity.Message;
import com.springboot.chatapp.domain.entity.Notification;
import com.springboot.chatapp.domain.entity.User;
import com.springboot.chatapp.domain.enumerate.NotificationType;
import com.springboot.chatapp.mapper.Mapper;
import com.springboot.chatapp.service.FriendshipService;
import com.springboot.chatapp.service.MessageService;
import com.springboot.chatapp.service.UserService;
import com.springboot.chatapp.utils.AppConstants;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper implements Mapper<Notification, NotificationResponseDTO> {
    private ModelMapper modelMapper;

    public NotificationMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;

    }
    @Override
    public NotificationResponseDTO mapToResponseDTO(Notification notification) {
        return modelMapper.map(notification, NotificationResponseDTO.class);
    }

    @Override
    public Notification mapToEntity(NotificationResponseDTO notificationResponseDto) {
        return modelMapper.map(notificationResponseDto, Notification.class);
    }
}