package com.springboot.chatapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public abstract class AbstractTestcontainersTest {

    @Container
    static MySQLContainer<?> mySQLContainer =
            new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
                    .withDatabaseName("chatapp")
                    .withUsername("root")
                    .withPassword("thongtranpc2@")
                    .withExposedPorts(3306);

    @Test
    void canEstablishConnection() {
        assertThat(mySQLContainer.isCreated()).isTrue();
        assertThat(mySQLContainer.isRunning()).isTrue();
    }
}
