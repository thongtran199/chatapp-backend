package com.springboot.chatapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.springboot.chatapp.model.entity.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByResetToken(String resetToken);
}
