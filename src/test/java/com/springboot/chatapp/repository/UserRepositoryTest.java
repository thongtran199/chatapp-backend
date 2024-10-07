package com.springboot.chatapp.repository;

import com.springboot.chatapp.AbstractTestcontainersTest;
import com.springboot.chatapp.TestDataUtils;
import com.springboot.chatapp.model.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest extends AbstractTestcontainersTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user1 = TestDataUtils.createUser("thongtran", "thongtran@gmail.com", "Tran Van Thong");
        User user2 = TestDataUtils.createUser("khanhvan", "khanhvan@gmail.com", "Tran Nguyen Khanh Van");
        userRepository.save(user1);
        userRepository.save(user2);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void findByEmail() {
        Optional<User> userByEmail = userRepository.findByEmail("thongtran@gmail.com");
        assertThat(userByEmail).isPresent();
        assertThat(userByEmail.get().getUsername()).isEqualTo("thongtran");
    }

    @Test
    void findByUsernameOrEmail() {
        Optional<User> user = userRepository.findByUsernameOrEmail("thongtran", "nonexistent@gmail.com");
        assertThat(user).isPresent();
        assertThat(user.get().getEmail()).isEqualTo("thongtran@gmail.com");

        Optional<User> user2 = userRepository.findByUsernameOrEmail("nonexistent", "khanhvan@gmail.com");
        assertThat(user2).isPresent();
        assertThat(user2.get().getUsername()).isEqualTo("khanhvan");
    }

    @Test
    void findByUsername() {
        Optional<User> userByUsername = userRepository.findByUsername("khanhvan");
        assertThat(userByUsername).isPresent();
        assertThat(userByUsername.get().getEmail()).isEqualTo("khanhvan@gmail.com");
    }

    @Test
    void existsByUsername() {
        assertThat(userRepository.existsByUsername("thongtran")).isTrue();
        assertThat(userRepository.existsByUsername("nonexistent")).isFalse();
    }

    @Test
    void existsByEmail() {
        assertThat(userRepository.existsByEmail("thongtran@gmail.com")).isTrue();
        assertThat(userRepository.existsByEmail("nonexistent@gmail.com")).isFalse();
    }

    @Test
    void findByFullNameContaining() {
        List<User> users = userRepository.findByFullNameContaining("Tran");
        assertThat(users).hasSize(2);
    }
}
