package com.example.assignment.course.application.usecae;

import com.example.assignment.common.annotaion.UseCase;
import com.example.assignment.course.application.command.CourseCommand;
import com.example.assignment.course.application.mapper.CourseMapper;
import com.example.assignment.course.domain.model.Course;
import com.example.assignment.course.domain.repository.CourseRepository;
import com.example.assignment.course.presentation.dto.CourseResponse;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class RegisterCourseUseCase {

    private final CourseRepository courseRepository;

    public CourseResponse execute(CourseCommand command) {
        Course course = CourseMapper.toDomain(command);
        Course result = courseRepository.save(course);

        return CourseResponse.from(result);
    }
}
