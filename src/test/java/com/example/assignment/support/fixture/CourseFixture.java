package com.example.assignment.support.fixture;

import com.example.assignment.course.domain.model.Course;

import java.time.LocalDateTime;


public class CourseFixture {
    public static Course givenCourse(){
        return Course.create(
                "course",
                "desc",
                1000,
                0,
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(7),
                LocalDateTime.now().plusDays(14)
        );
    }
}
