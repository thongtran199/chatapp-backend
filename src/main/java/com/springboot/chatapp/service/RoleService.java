package com.springboot.chatapp.service;

import com.springboot.chatapp.model.dto.role.RoleRequestDto;
import com.springboot.chatapp.model.entity.Role;

import java.util.List;

public interface RoleService {
    Role saveRole(RoleRequestDto roleRequestDTO);

    Role findById(Long roleId);

    Role findByName(String name);

    List<Role> findAllRoles();

    boolean existsById(Long roleId);
}