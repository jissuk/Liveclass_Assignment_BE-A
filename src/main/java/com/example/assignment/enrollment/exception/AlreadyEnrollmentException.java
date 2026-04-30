package com.example.assignment.enrollment.exception;

public class AlreadyEnrollmentException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "이미 구매한 강의입니다.";
    public AlreadyEnrollmentException() {
        super(DEFAULT_MESSAGE);
    }
}
