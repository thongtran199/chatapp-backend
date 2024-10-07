package com.springboot.chatapp.service;

import com.springboot.chatapp.model.entity.PasswordResetToken;
import com.springboot.chatapp.model.entity.User;
import com.springboot.chatapp.model.exception.ResourceNotFoundException;
import com.springboot.chatapp.model.dto.passwordReset.NewPasswordResetTokenDto;
import com.springboot.chatapp.repository.PasswordResetTokenRepository;
import com.springboot.chatapp.service.impl.PasswordResetTokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PasswordResetTokenServiceTest {

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PasswordResetTokenServiceImpl passwordResetTokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save() {
        NewPasswordResetTokenDto newTokenDto = new NewPasswordResetTokenDto();
        newTokenDto.setUserId(1L);
        newTokenDto.setResetToken("resetToken123");
        newTokenDto.setExpiresAt(LocalDateTime.now().plusHours(1));

        User user = new User();
        user.setUserId(1L);

        when(userService.findById(1L)).thenReturn(user);
        when(passwordResetTokenRepository.save(any(PasswordResetToken.class))).thenReturn(new PasswordResetToken());

        PasswordResetToken result = passwordResetTokenService.save(newTokenDto);

        assertNotNull(result);
        verify(userService, times(1)).findById(1L);
        verify(passwordResetTokenRepository, times(1)).save(any(PasswordResetToken.class));
    }

    @Test
    void findById() {
        PasswordResetToken token = new PasswordResetToken();
        token.setTokenId(1L);

        when(passwordResetTokenRepository.findById(1L)).thenReturn(Optional.of(token));

        PasswordResetToken result = passwordResetTokenService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getTokenId());
        verify(passwordResetTokenRepository, times(1)).findById(1L);
    }

    @Test
    void findById_NotFound() {
        when(passwordResetTokenRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> passwordResetTokenService.findById(1L));
        verify(passwordResetTokenRepository, times(1)).findById(1L);
    }

    @Test
    void findByToken() {
        PasswordResetToken token = new PasswordResetToken();
        token.setResetToken("resetToken123");

        when(passwordResetTokenRepository.findByResetToken("resetToken123")).thenReturn(Optional.of(token));

        PasswordResetToken result = passwordResetTokenService.findByToken("resetToken123");

        assertNotNull(result);
        assertEquals("resetToken123", result.getResetToken());
        verify(passwordResetTokenRepository, times(1)).findByResetToken("resetToken123");
    }

    @Test
    void findByToken_NotFound() {
        when(passwordResetTokenRepository.findByResetToken("resetToken123")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> passwordResetTokenService.findByToken("resetToken123"));
        verify(passwordResetTokenRepository, times(1)).findByResetToken("resetToken123");
    }

    @Test
    void deleteToken() {
        when(passwordResetTokenRepository.existsById(1L)).thenReturn(true);

        passwordResetTokenService.deleteToken(1L);

        verify(passwordResetTokenRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteToken_NotFound() {
        when(passwordResetTokenRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> passwordResetTokenService.deleteToken(1L));
        verify(passwordResetTokenRepository, times(1)).existsById(1L);
    }

    @Test
    void existsById() {
        when(passwordResetTokenRepository.existsById(1L)).thenReturn(true);

        boolean result = passwordResetTokenService.existsById(1L);

        assertTrue(result);
        verify(passwordResetTokenRepository, times(1)).existsById(1L);
    }
}
