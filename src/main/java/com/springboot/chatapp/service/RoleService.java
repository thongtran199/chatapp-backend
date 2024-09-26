package com.springboot.chatapp.service;

import com.springboot.chatapp.domain.dto.user.request.RoleRequestDTO;
import com.springboot.chatapp.domain.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Role saveRole(RoleRequestDTO roleRequestDTO);

    Role findById(Long roleId);

    Role findByName(String name);

    List<Role> findAllRoles();

    boolean existsById(Long roleId);
}