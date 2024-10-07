package com.springboot.chatapp.repository;

import com.springboot.chatapp.TestDataUtils;
import com.springboot.chatapp.model.entity.PasswordResetToken;
import com.springboot.chatapp.model.entity.Role;
import com.springboot.chatapp.model.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PasswordResetTokenRepositoryTest {
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = TestDataUtils.createUser("thongtran", "thongtran@gmail.com", "Tran Van Thong");
        userRepository.save(user);

        PasswordResetToken passwordResetToken = TestDataUtils.passwordResetToken(user, "abc");
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @AfterEach
    void tearDown() {
        passwordResetTokenRepository.deleteAll();
    }

    @Test
    void findByResetToken() {
        Optional<PasswordResetToken> optional = passwordResetTokenRepository.findByResetToken("abc");
        assertThat(optional).isPresent();
        assertThat(optional.get().getResetToken()).isEqualTo("abc");
    }
}