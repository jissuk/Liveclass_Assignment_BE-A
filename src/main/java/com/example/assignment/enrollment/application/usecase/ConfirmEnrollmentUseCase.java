package com.example.assignment.enrollment.application.usecase;

import com.example.assignment.common.annotaion.UseCase;
import com.example.assignment.enrollment.domain.model.Enrollment;
import com.example.assignment.enrollment.domain.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@UseCase
@RequiredArgsConstructor
public class ConfirmEnrollmentUseCase {

    private final EnrollmentRepository enrollmentRepository;

    public void execute(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId);
        enrollment.confirm(LocalDateTime.now());
    }
}
