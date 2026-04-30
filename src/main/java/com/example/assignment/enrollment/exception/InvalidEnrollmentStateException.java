package com.example.assignment.enrollment.exception;

import com.example.assignment.enrollment.domain.model.EnrollmentAction;
import com.example.assignment.enrollment.domain.model.EnrollmentStatus;

public class InvalidEnrollmentStateException extends RuntimeException {
    public InvalidEnrollmentStateException(EnrollmentStatus status, EnrollmentAction action) {
        super("상태가 " + action + "일 때 " + status + " 액션을 수행할 수 없습니다.");
    }
}