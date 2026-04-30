package com.example.assignment.enrollment.infrastructure.jpa;

import com.example.assignment.enrollment.domain.model.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaEnrollmentRepository extends JpaRepository<Enrollment,Long> {
    @Query("select e from Enrollment e where e.user.id = :userId")
    Page<Enrollment> findByUserId(@Param("userId") Long userId, Pageable pageable);
}
