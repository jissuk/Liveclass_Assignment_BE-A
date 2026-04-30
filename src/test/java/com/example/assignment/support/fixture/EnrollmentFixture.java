package com.example.assignment.support.fixture;

import com.example.assignment.course.domain.model.Course;
import com.example.assignment.enrollment.domain.model.Enrollment;
import com.example.assignment.user.domain.model.User;

import java.time.LocalDateTime;

public class EnrollmentFixture {
    public static Enrollment givenEnrollment(){
        Course course = Course.create(
                "name",
                "description",
                30_000,
                10,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
        );
        User user = User.create("user");
        return Enrollment.create(user, course);
    }
}
