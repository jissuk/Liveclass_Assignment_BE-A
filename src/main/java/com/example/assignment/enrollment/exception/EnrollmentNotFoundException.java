package com.example.assignment.enrollment.exception;

public class EnrollmentNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "수강중인 강의를 찾을 수 없습니다.";
    public EnrollmentNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
