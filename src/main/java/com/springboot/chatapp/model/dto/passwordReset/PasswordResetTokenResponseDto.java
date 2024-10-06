package com.springboot.chatapp.model.dto.passwordReset;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PasswordResetTokenResponseDto {
    private Long tokenId;
    private Long userId;
    private String token;
    private LocalDateTime expiryDate;
}