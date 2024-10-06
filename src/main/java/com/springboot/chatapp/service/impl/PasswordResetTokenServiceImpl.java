package com.springboot.chatapp.service.impl;

import com.springboot.chatapp.model.entity.PasswordResetToken;
import com.springboot.chatapp.model.entity.User;
import com.springboot.chatapp.model.exception.ResourceNotFoundException;
import com.springboot.chatapp.model.dto.passwordReset.NewPasswordResetTokenDto;
import com.springboot.chatapp.repository.PasswordResetTokenRepository;
import com.springboot.chatapp.service.PasswordResetTokenService;
import com.springboot.chatapp.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserService userService;

    public PasswordResetTokenServiceImpl(
            PasswordResetTokenRepository passwordResetTokenRepository,
            UserService userService) {

        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userService = userService;
    }

    @Override
    public PasswordResetToken save(NewPasswordResetTokenDto newPasswordResetTokenDTO) {
        PasswordResetToken token = new PasswordResetToken();

        User user = userService.findById(newPasswordResetTokenDTO.getUserId());
        token.setUser(user);
        token.setResetToken(newPasswordResetTokenDTO.getResetToken());
        token.setExpiresAt(newPasswordResetTokenDTO.getExpiresAt());
        return passwordResetTokenRepository.save(token);
    }

    @Override
    public PasswordResetToken findById(Long tokenId) {
        return passwordResetTokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("PasswordResetToken", "tokenId", tokenId));
    }

    @Override
    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenRepository.findByResetToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("PasswordResetToken", "token", token));
    }

    @Override
    public void deleteToken(Long tokenId) {
        if (!passwordResetTokenRepository.existsById(tokenId)) {
            throw new ResourceNotFoundException("PasswordResetToken", "tokenId", tokenId);
        }
        passwordResetTokenRepository.deleteById(tokenId);
    }

    @Override
    public boolean existsById(Long tokenId) {
        return passwordResetTokenRepository.existsById(tokenId);
    }
}
