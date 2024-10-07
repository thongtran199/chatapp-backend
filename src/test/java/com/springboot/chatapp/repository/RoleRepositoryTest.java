package com.springboot.chatapp.repository;

import com.springboot.chatapp.TestDataUtils;
import com.springboot.chatapp.model.entity.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleRepositoryTest {
    @Autowired
    RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        Role role1 = TestDataUtils.createRole("ROLE_TEST");
        roleRepository.save(role1);
    }

    @AfterEach
    void tearDown() {
        roleRepository.deleteAll();
    }

    @Test
    void findByName() {
        Optional<Role> foundRole = roleRepository.findByName("ROLE_TEST");
        assertThat(foundRole).isPresent();
        assertThat(foundRole.get().getName()).isEqualTo("ROLE_TEST");
    }
}