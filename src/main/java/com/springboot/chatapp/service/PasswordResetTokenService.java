package com.springboot.chatapp.service;

import com.springboot.chatapp.model.entity.PasswordResetToken;
import com.springboot.chatapp.model.dto.passwordReset.NewPasswordResetTokenDto;

public interface PasswordResetTokenService {
    PasswordResetToken save(NewPasswordResetTokenDto newPasswordResetTokenDTO);

    PasswordResetToken findById(Long tokenId);

    PasswordResetToken findByToken(String token);

    void deleteToken(Long tokenId);

    boolean existsById(Long tokenId);
}
