package com.example.assignment.course.application.mapper;

import com.example.assignment.course.application.command.CourseCommand;
import com.example.assignment.course.domain.model.Course;

public class CourseMapper {

    public static Course toDomain(CourseCommand command) {
        return Course.create(
                command.name(),
                command.description(),
                command.price(),
                command.capacity(),
                command.startDate(),
                command.endDate()
        );
    }
}
