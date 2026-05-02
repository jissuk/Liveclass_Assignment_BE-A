package com.example.assignment.support.fixture;

import com.example.assignment.course.domain.model.Course;
import com.example.assignment.enrollment.domain.model.Enrollment;
import com.example.assignment.user.domain.model.User;

import java.time.LocalDateTime;

public class EnrollmentFixture {
    public static Enrollment givenEnrollment(){
        Course course = Course.create(
                "course",
                "desc",
                1000,
                0,
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(7),
                LocalDateTime.now().plusDays(14)
        );
        User user = User.create("user");
        return Enrollment.create(user, course);
    }
}
