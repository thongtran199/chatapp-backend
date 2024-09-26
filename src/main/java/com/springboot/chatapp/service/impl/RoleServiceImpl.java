package com.springboot.chatapp.service.impl;

import com.springboot.chatapp.domain.dto.user.request.RoleRequestDTO;
import com.springboot.chatapp.domain.entity.Role;
import com.springboot.chatapp.exception.ResourceNotFoundException;
import com.springboot.chatapp.repository.RoleRepository;
import com.springboot.chatapp.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role saveRole(RoleRequestDTO roleRequestDTO) {
        Role role = new Role();
        role.setName(roleRequestDTO.getName());
        return roleRepository.save(role);
    }

    @Override
    public Role findById(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "roleId", roleId));
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", name));
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public boolean existsById(Long roleId) {
        return roleRepository.existsById(roleId);
    }
}
