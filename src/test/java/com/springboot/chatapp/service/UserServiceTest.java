package com.springboot.chatapp.service;

import com.springboot.chatapp.model.entity.User;
import com.springboot.chatapp.model.exception.ResourceNotFoundException;
import com.springboot.chatapp.repository.UserRepository;
import com.springboot.chatapp.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.springboot.chatapp.TestDataUtils.createUser;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = createUser("thongtran", "thongtran@gmail.com", "Tran Van Thong");
        user1.setUserId(1L);

        user2 = createUser("khanhvan", "khanhvan@gmail.com", "Tran Nguyen Khanh Van");
        user2.setUserId(2L);
    }

    @Test
    void findByUsernameOrEmail_foundByUsername() {
        when(userRepository.findByUsernameOrEmail("thongtran", "nonexistent@gmail.com"))
                .thenReturn(Optional.of(user1));

        User foundUser = userService.findByUsernameOrEmail("thongtran", "nonexistent@gmail.com");

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("thongtran");
        verify(userRepository, times(1)).findByUsernameOrEmail("thongtran", "nonexistent@gmail.com");
    }

    @Test
    void findByUsernameOrEmail_foundByEmail() {
        when(userRepository.findByUsernameOrEmail("nonexistent", "khanhvan@gmail.com"))
                .thenReturn(Optional.of(user2));

        User foundUser = userService.findByUsernameOrEmail("nonexistent", "khanhvan@gmail.com");

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("khanhvan@gmail.com");
        verify(userRepository, times(1)).findByUsernameOrEmail("nonexistent", "khanhvan@gmail.com");
    }

    @Test
    void findByUsernameOrEmail_notFound() {
        when(userRepository.findByUsernameOrEmail(anyString(), anyString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByUsernameOrEmail("nonexistent", "nonexistent@gmail.com"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User")
                .hasMessageContaining("username or email")
                .hasMessageContaining("nonexistent, nonexistent@gmail.com");

        verify(userRepository, times(1)).findByUsernameOrEmail("nonexistent", "nonexistent@gmail.com");
    }

    @Test
    void findByUsername_found() {
        when(userRepository.findByUsername("thongtran"))
                .thenReturn(Optional.of(user1));

        User foundUser = userService.findByUsername("thongtran");

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("thongtran");
        verify(userRepository, times(1)).findByUsername("thongtran");
    }

    @Test
    void findByUsername_notFound() {
        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByUsername("nonexistent"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User")
                .hasMessageContaining("username")
                .hasMessageContaining("nonexistent");

        verify(userRepository, times(1)).findByUsername("nonexistent");
    }

    @Test
    void findById_found() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user1));

        User foundUser = userService.findById(1L);

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUserId()).isEqualTo(1L);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findById_notFound() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User")
                .hasMessageContaining("id")
                .hasMessageContaining("99");

        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    void findByEmail_found() {
        when(userRepository.findByEmail("thongtran@gmail.com"))
                .thenReturn(Optional.of(user1));

        User foundUser = userService.findByEmail("thongtran@gmail.com");

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("thongtran@gmail.com");
        verify(userRepository, times(1)).findByEmail("thongtran@gmail.com");
    }

    @Test
    void findByEmail_notFound() {
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByEmail("nonexistent@gmail.com"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User")
                .hasMessageContaining("email")
                .hasMessageContaining("nonexistent@gmail.com");

        verify(userRepository, times(1)).findByEmail("nonexistent@gmail.com");
    }

    @Test
    void existsByUsername_true() {
        when(userRepository.existsByUsername("thongtran"))
                .thenReturn(true);

        Boolean exists = userService.existsByUsername("thongtran");

        assertThat(exists).isTrue();
        verify(userRepository, times(1)).existsByUsername("thongtran");
    }

    @Test
    void existsByUsername_false() {
        when(userRepository.existsByUsername("nonexistent"))
                .thenReturn(false);

        Boolean exists = userService.existsByUsername("nonexistent");

        assertThat(exists).isFalse();
        verify(userRepository, times(1)).existsByUsername("nonexistent");
    }

    @Test
    void existsByEmail_true() {
        when(userRepository.existsByEmail("thongtran@gmail.com"))
                .thenReturn(true);

        Boolean exists = userService.existsByEmail("thongtran@gmail.com");

        assertThat(exists).isTrue();
        verify(userRepository, times(1)).existsByEmail("thongtran@gmail.com");
    }

    @Test
    void existsByEmail_false() {
        when(userRepository.existsByEmail("nonexistent@gmail.com"))
                .thenReturn(false);

        Boolean exists = userService.existsByEmail("nonexistent@gmail.com");

        assertThat(exists).isFalse();
        verify(userRepository, times(1)).existsByEmail("nonexistent@gmail.com");
    }

    @Test
    void findByFullNameContaining_found() {
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findByFullNameContaining("Tran"))
                .thenReturn(users);

        List<User> foundUsers = userService.findByFullNameContaining("Tran");

        assertThat(foundUsers).isNotEmpty();
        assertThat(foundUsers).hasSize(2);
        verify(userRepository, times(1)).findByFullNameContaining("Tran");
    }

    @Test
    void findByFullNameContaining_notFound() {
        when(userRepository.findByFullNameContaining("Nonexistent"))
                .thenReturn(Arrays.asList());

        List<User> foundUsers = userService.findByFullNameContaining("Nonexistent");

        assertThat(foundUsers).isEmpty();
        verify(userRepository, times(1)).findByFullNameContaining("Nonexistent");
    }

    @Test
    void save_success() {
        User newUser = createUser("newuser", "newuser@gmail.com", "New User");
        when(userRepository.save(any(User.class)))
                .thenReturn(newUser);

        User savedUser = userService.save(newUser);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("newuser");
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser.getUsername()).isEqualTo("newuser");
        assertThat(capturedUser.getEmail()).isEqualTo("newuser@gmail.com");
        assertThat(capturedUser.getFullName()).isEqualTo("New User");
    }
}