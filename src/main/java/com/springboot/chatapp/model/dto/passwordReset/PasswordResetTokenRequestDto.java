package com.springboot.chatapp.model.dto.passwordReset;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetTokenRequestDto {
    private Long userId;
}