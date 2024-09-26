package com.springboot.chatapp.domain.dto.user.request;

import lombok.Data;

@Data
public class MessageRequestDTO {
    private Long messageSenderId;
    private Long messageReceiverId;
    private String content;
}
