package com.example.assignment.course.application.usecae;

import com.example.assignment.common.annotaion.UseCase;
import com.example.assignment.course.presentation.dto.CourseResponse;
import com.example.assignment.course.domain.model.Course;
import com.example.assignment.course.domain.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class GetCourseUseCase {

    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public CourseResponse execute(Long courseId) {
        Course course = courseRepository.findById(courseId);

        return CourseResponse.from(course);
    }
}
