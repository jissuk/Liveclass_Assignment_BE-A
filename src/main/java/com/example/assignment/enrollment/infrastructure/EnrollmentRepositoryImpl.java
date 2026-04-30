package com.example.assignment.enrollment.infrastructure;

import com.example.assignment.enrollment.domain.model.Enrollment;
import com.example.assignment.enrollment.domain.repository.EnrollmentRepository;
import com.example.assignment.enrollment.exception.EnrollmentNotFoundException;
import com.example.assignment.enrollment.infrastructure.jpa.JpaEnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EnrollmentRepositoryImpl implements EnrollmentRepository {

    private final JpaEnrollmentRepository jpaEnrollmentRepository;

    @Override
    public Page<Enrollment> findByUserId(Long userId, Pageable pageable) {
        return jpaEnrollmentRepository.findByUserId(userId, pageable);
    }

    @Override
    public Enrollment save(Enrollment enrollment) {
        return jpaEnrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment findById(Long enrollmentId) {
        return jpaEnrollmentRepository.findById(enrollmentId)
                .orElseThrow(EnrollmentNotFoundException::new);
    }
}
