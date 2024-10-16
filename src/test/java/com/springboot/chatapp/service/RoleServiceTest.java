package com.springboot.chatapp.service;

import com.springboot.chatapp.model.dto.role.RoleRequestDto;
import com.springboot.chatapp.model.entity.Role;
import com.springboot.chatapp.model.exception.ResourceNotFoundException;
import com.springboot.chatapp.repository.RoleRepository;
import com.springboot.chatapp.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Captor
    ArgumentCaptor<Role> roleArgumentCaptor;

    @BeforeEach
    void setUp() {
        roleService = new RoleServiceImpl(roleRepository);
    }

    @Test
    void saveRole() {
        RoleRequestDto roleRequestDto = new RoleRequestDto();
        roleRequestDto.setName("ROLE_USER");
        Role role = new Role();
        role.setName("ROLE_USER");

        when(roleRepository.save(any(Role.class))).thenReturn(role);

        Role savedRole = roleService.saveRole(roleRequestDto);

        assertThat(savedRole).isNotNull();
        assertThat(savedRole.getName()).isEqualTo("ROLE_USER");

        verify(roleRepository).save(roleArgumentCaptor.capture());

        Role capturedRole = roleArgumentCaptor.getValue();
        assertThat(capturedRole.getName()).isEqualTo("ROLE_USER");
    }

    @Test
    void findById() {
        Role role = new Role();
        role.setRoleId(1L);
        role.setName("ROLE_USER");

        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));

        Role foundRole = roleService.findById(1L);

        assertThat(foundRole).isNotNull();
        assertThat(foundRole.getRoleId()).isEqualTo(1L);
        assertThat(foundRole.getName()).isEqualTo("ROLE_USER");

        verify(roleRepository, times(1)).findById(anyLong());
    }

    @Test
    void findById_NotFound() {
        
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roleService.findById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Role")
                .hasMessageContaining("roleId")
                .hasMessageContaining("1");

        verify(roleRepository, times(1)).findById(anyLong());
    }

    @Test
    void findByName() {
        
        Role role = new Role();
        role.setName("ROLE_ADMIN");

        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(role));

        Role foundRole = roleService.findByName("ROLE_ADMIN");

        assertThat(foundRole).isNotNull();
        assertThat(foundRole.getName()).isEqualTo("ROLE_ADMIN");

        verify(roleRepository, times(1)).findByName(anyString());
    }

    @Test
    void findByName_NotFound() {
        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roleService.findByName("ROLE_USER"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Role")
                .hasMessageContaining("name")
                .hasMessageContaining("ROLE_USER");

        verify(roleRepository, times(1)).findByName(anyString());
    }

    @Test
    void findAllRoles() {
        
        Role role1 = new Role();
        role1.setName("ROLE_ADMIN");

        Role role2 = new Role();
        role2.setName("ROLE_USER");

        when(roleRepository.findAll()).thenReturn(List.of(role1, role2));

        List<Role> roles = roleService.findAllRoles();

        assertThat(roles).hasSize(2);
        assertThat(roles.get(0).getName()).isEqualTo("ROLE_ADMIN");
        assertThat(roles.get(1).getName()).isEqualTo("ROLE_USER");

        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void existsById() {
        
        when(roleRepository.existsById(anyLong())).thenReturn(true);

        boolean exists = roleService.existsById(1L);

        assertThat(exists).isTrue();
        verify(roleRepository, times(1)).existsById(anyLong());
    }

    @Test
    void createRoleIfNotExists_RoleExists() {
        
        Role role = new Role();
        role.setName("ROLE_USER");

        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(role));

        roleService.createRoleIfNotExists("ROLE_USER");

        verify(roleRepository, times(1)).findByName(anyString());
        verify(roleRepository, times(0)).save(any(Role.class)); // Không lưu vì role đã tồn tại
    }

    @Test
    void createRoleIfNotExists_RoleNotExists() {
        
        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

        RoleRequestDto roleRequestDto = new RoleRequestDto();
        roleRequestDto.setName("ROLE_USER");
        Role role = new Role();
        role.setName("ROLE_USER");

        when(roleRepository.save(any(Role.class))).thenReturn(role);

        roleService.createRoleIfNotExists("ROLE_USER");

        verify(roleRepository, times(1)).findByName(anyString());
        verify(roleRepository, times(1)).save(any(Role.class));
    }
}
