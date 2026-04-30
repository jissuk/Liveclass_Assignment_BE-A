package com.example.assignment.enrollment.application.command;

import lombok.Builder;

@Builder
public record EnrollmentCommand(
        Long userId,
        Long courseId
) {
}
