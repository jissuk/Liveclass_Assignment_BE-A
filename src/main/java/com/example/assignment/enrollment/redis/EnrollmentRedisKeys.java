package com.example.assignment.enrollment.redis;

public class EnrollmentRedisKeys {

    private static final String ENROLLMENT_PREFIX = "enrollment:";

    /**
     * 특정 과목의 선착순 대기열 (Sorted Set)
     * Key: enrollment:course:{courseId}:queue
     * Value: userId
     * Score: timestamp
     */
    public static String getQueueKey(Long courseId) {
        return String.format("%scourse:%d:queue", ENROLLMENT_PREFIX, courseId);
    }
}
