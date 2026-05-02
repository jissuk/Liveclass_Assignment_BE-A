package com.example.assignment.enrollment.infrastructure.redis;

import com.example.assignment.enrollment.application.port.out.EnrollmentQueuePort;
import com.example.assignment.enrollment.exception.AlreadyEnrollmentException;
import com.example.assignment.enrollment.redis.EnrollmentRedisKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

@RequiredArgsConstructor
public class RedisEnrollmentQueueAdapter implements EnrollmentQueuePort {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void add(Long courseId, Long userId, long timestamp) {
        String queueKey = getQueueKey(courseId);

        Boolean result = redisTemplate.opsForZSet().add(queueKey, userId.toString(), (double) timestamp);

        if (Boolean.FALSE.equals(result)) {
            throw new AlreadyEnrollmentException();
        }
    }

    @Override
    public Long getRank(Long courseId, Long userId) {
        String queueKey = getQueueKey(courseId);
        return redisTemplate.opsForZSet().rank(queueKey, userId.toString());
    }

    @Override
    public void remove(Long courseId, Long userId) {
        String queueKey = getQueueKey(courseId);
        redisTemplate.opsForZSet().remove(queueKey, userId.toString());
    }

    private String getQueueKey(Long courseId) {
        return EnrollmentRedisKeys.getQueueKey(courseId);
    }
}