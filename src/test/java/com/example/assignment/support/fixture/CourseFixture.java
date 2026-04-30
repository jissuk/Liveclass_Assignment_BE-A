package com.example.assignment.support.fixture;

import com.example.assignment.course.domain.model.Course;

import java.time.LocalDateTime;


public class CourseFixture {
    public static Course givenCourse(){
        return Course.create(
                "name",
                "description",
                30_000,
                10,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
        );
    }
}
