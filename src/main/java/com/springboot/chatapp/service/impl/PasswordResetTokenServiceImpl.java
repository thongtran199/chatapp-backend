package com.springboot.chatapp.service.impl;

import com.springboot.chatapp.domain.dto.user.request.PasswordResetTokenRequestDTO;
import com.springboot.chatapp.domain.entity.PasswordResetToken;
import com.springboot.chatapp.domain.entity.User;
import com.springboot.chatapp.exception.ResourceNotFoundException;
import com.springboot.chatapp.payload.notification.NewPasswordResetTokenDTO;
import com.springboot.chatapp.repository.PasswordResetTokenRepository;
import com.springboot.chatapp.service.PasswordResetTokenService;
import com.springboot.chatapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private UserService userService;
    @Override
    public PasswordResetToken save(NewPasswordResetTokenDTO newPasswordResetTokenDTO) {
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
