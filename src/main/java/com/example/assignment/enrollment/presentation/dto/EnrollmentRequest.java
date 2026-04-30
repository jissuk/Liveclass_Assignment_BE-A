package com.example.assignment.enrollment.presentation.dto;

import com.example.assignment.enrollment.application.command.EnrollmentCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EnrollmentRequest(
    @NotNull(message = "유저 ID는 필수입니다.")
    @Min( value = 1, message ="유저 ID는 1 이상이어야 합니다.")
    Long userId,

    @NotNull(message = "강의 ID는 필수입니다.")
    @Min( value = 1, message ="강의 ID는 1 이상이어야 합니다.")
    Long courseId
) {
    public EnrollmentCommand toCommand() {
        return new EnrollmentCommand(userId, courseId);
    }
}
