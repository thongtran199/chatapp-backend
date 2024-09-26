package com.springboot.chatapp.service;

import com.springboot.chatapp.domain.dto.user.request.PasswordResetTokenRequestDTO;
import com.springboot.chatapp.domain.dto.user.response.PasswordResetTokenResponseDTO;
import com.springboot.chatapp.domain.entity.PasswordResetToken;
import com.springboot.chatapp.payload.notification.NewPasswordResetTokenDTO;

import java.util.Optional;

public interface PasswordResetTokenService {
    PasswordResetToken save(NewPasswordResetTokenDTO newPasswordResetTokenDTO);

    PasswordResetToken findById(Long tokenId);

    PasswordResetToken findByToken(String token);

    void deleteToken(Long tokenId);

    boolean existsById(Long tokenId);
}
