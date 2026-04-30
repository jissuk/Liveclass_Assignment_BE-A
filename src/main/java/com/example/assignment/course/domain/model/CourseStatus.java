package com.example.assignment.course.domain.model;

import com.example.assignment.course.exception.InvalidCourseStateException;

public enum CourseStatus {

    DRAFT {
        @Override
        public CourseStatus open() {
            return OPEN;
        }
    },

    OPEN {
        @Override
        public CourseStatus close() {
            return CLOSED;
        }
    },

    CLOSED;

    public CourseStatus open() {
        throw new InvalidCourseStateException(this, CourseAction.OPEN);
    }

    public CourseStatus close() {
        throw new InvalidCourseStateException(this, CourseAction.CLOSE);
    }
}