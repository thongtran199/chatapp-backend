package com.springboot.chatapp.domain.dto.user.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PasswordResetTokenRequestDTO {
    private Long userId;
}