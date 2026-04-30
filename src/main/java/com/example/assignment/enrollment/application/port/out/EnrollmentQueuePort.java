package com.example.assignment.enrollment.application.port.out;

public interface EnrollmentQueuePort {
    void add(Long courseId, Long userId, long timestamp);
    Long getRank(Long courseId, Long userId);
}
