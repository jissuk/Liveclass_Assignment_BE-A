package com.example.assignment.enrollment.presentation.dto;

import com.example.assignment.enrollment.domain.model.Enrollment;
import com.example.assignment.enrollment.domain.model.EnrollmentStatus;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EnrollmentResponse(
    EnrollmentStatus status,
    LocalDate createdAt,
    boolean isWaitlisted,
    Long waitNumber
) {
    public static EnrollmentResponse from(Enrollment enrollment) {
        return EnrollmentResponse.builder()
                .status(enrollment.getStatus())
                .createdAt(enrollment.getCreatedAt())
                .isWaitlisted(false)
                .build();
    }

    // 대기열 등록
    public static EnrollmentResponse waiting(Long waitNumber) {
        return EnrollmentResponse.builder()
                                    .waitNumber(waitNumber)
                                    .isWaitlisted(true)
                                    .build();
    }
}