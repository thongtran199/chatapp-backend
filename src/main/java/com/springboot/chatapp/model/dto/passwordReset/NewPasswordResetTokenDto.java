package com.springboot.chatapp.model.dto.passwordReset;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class NewPasswordResetTokenDto {
    private Long userId;
    private String resetToken;
    private LocalDateTime expiresAt;
}