package com.example.assignment.enrollment.domain.repository;

import com.example.assignment.enrollment.domain.model.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EnrollmentRepository {
    Page<Enrollment> findByUserId(Long userId, Pageable pageable);
    Enrollment save(Enrollment enrollment);
    Enrollment findById(Long enrollmentId);
}
