package com.example.assignment.enrollment.infrastructure.config;

import com.example.assignment.enrollment.application.port.out.EnrollmentQueuePort;
import com.example.assignment.enrollment.infrastructure.redis.RedisEnrollmentQueueAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class EnrollmentInventoryConfig {

    @Bean
    public EnrollmentQueuePort enrollmentInventory(RedisTemplate<String, String> redisTemplate) {
        return new RedisEnrollmentQueueAdapter(redisTemplate);
    }
}