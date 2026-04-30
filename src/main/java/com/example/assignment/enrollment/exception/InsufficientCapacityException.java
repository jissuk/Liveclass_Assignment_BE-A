package com.example.assignment.enrollment.exception;

public class InsufficientCapacityException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "해당 수강의 정원이 마감되었습니다.";

    public InsufficientCapacityException() {
        super(DEFAULT_MESSAGE);
    }
}
