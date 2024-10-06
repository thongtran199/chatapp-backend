package com.springboot.chatapp.service;

import com.springboot.chatapp.model.entity.User;

import java.util.List;

public interface UserService {

    User findByUsernameOrEmail(String username, String email);

    User findByUsername(String username);

    User findById(Long userId);

    User findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    List<User> findByFullNameContaining(String fullName);
    User save(User user);

}
