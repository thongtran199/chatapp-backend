package com.springboot.chatapp.domain.dto.user.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PasswordResetTokenResponseDTO {
    private Long tokenId;
    private Long userId;
    private String token;
    private LocalDateTime expiryDate;
}