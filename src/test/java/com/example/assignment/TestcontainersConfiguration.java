package com.example.assignment;

import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
public class TestcontainersConfiguration {

    public static final MySQLContainer<?> MYSQL_CONTAINER;

    public static final GenericContainer<?> REDIS_CONTAINER;

    static {
        // MySQL ===================================
        MYSQL_CONTAINER = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
                .withDatabaseName("liveklass")
                .withUsername("root")
                .withPassword("1234");
        MYSQL_CONTAINER.start();

        System.setProperty("spring.datasource.url", MYSQL_CONTAINER.getJdbcUrl() + "?characterEncoding=UTF-8&serverTimezone=Asia/Seoul");
        System.setProperty("spring.datasource.username", MYSQL_CONTAINER.getUsername());
        System.setProperty("spring.datasource.password", MYSQL_CONTAINER.getPassword());
        // ==========================================

        // Redis ===================================
        REDIS_CONTAINER = new GenericContainer<>(DockerImageName.parse("redis:7.2.4-alpine"))
                .withExposedPorts(6379)
                .withCommand("redis-server --appendonly no")
                .withReuse(true);
        REDIS_CONTAINER.start();

        System.setProperty("spring.data.redis.host", REDIS_CONTAINER.getHost());
        System.setProperty("spring.data.redis.port", REDIS_CONTAINER.getMappedPort(6379).toString());
        // ==========================================

    }
}
