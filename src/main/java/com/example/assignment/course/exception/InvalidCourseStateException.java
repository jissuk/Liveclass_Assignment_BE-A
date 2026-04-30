package com.example.assignment.course.exception;

import com.example.assignment.course.domain.model.CourseAction;
import com.example.assignment.course.domain.model.CourseStatus;

public class InvalidCourseStateException extends RuntimeException {
    public InvalidCourseStateException(CourseStatus status, CourseAction action) {
        super("상태가 " + action + "일 때 " + status + " 액션을 수행할 수 없습니다.");
    }
}
