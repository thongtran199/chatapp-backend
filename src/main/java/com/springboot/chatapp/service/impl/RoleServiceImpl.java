package com.springboot.chatapp.service.impl;

import com.springboot.chatapp.model.dto.role.RoleRequestDto;
import com.springboot.chatapp.model.entity.Role;
import com.springboot.chatapp.model.exception.ResourceNotFoundException;
import com.springboot.chatapp.repository.RoleRepository;
import com.springboot.chatapp.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {


    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role saveRole(RoleRequestDto roleRequestDTO) {
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
