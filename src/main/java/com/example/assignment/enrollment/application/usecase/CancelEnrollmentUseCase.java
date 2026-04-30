package com.example.assignment.enrollment.application.usecase;

import com.example.assignment.common.annotaion.UseCase;
import com.example.assignment.enrollment.domain.model.Enrollment;
import com.example.assignment.enrollment.domain.repository.EnrollmentRepository;
import com.example.assignment.enrollment.presentation.dto.EnrollmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@UseCase
@RequiredArgsConstructor
public class CancelEnrollmentUseCase {

    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public void execute(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId);
        enrollment.cancel(LocalDateTime.now());
    }
}