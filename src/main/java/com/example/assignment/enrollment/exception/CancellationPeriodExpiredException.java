package com.example.assignment.enrollment.exception;

public class CancellationPeriodExpiredException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "취소 가능 기간(7일)이 지났습니다.";
    public CancellationPeriodExpiredException() {
        super(DEFAULT_MESSAGE);
    }
}
