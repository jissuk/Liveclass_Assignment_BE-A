package com.example.assignment.course.exception;

public class CourseNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "강의 조회에 실패하였습니다.";

    public CourseNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
