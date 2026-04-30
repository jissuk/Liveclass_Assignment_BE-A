package com.example.assignment.course.application.usecae;

import com.example.assignment.common.annotaion.UseCase;
import com.example.assignment.course.domain.model.Course;
import com.example.assignment.course.domain.model.CourseStatus;
import com.example.assignment.course.domain.repository.CourseRepository;
import com.example.assignment.course.presentation.dto.CourseResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class GetCourseListUseCase {

    private final CourseRepository courseRepository;

    public List<CourseResponse> execute(List<CourseStatus> statuses) {
        List<Course> courses;

        if (statuses == null || statuses.isEmpty()) {
            courses = courseRepository.findAll();
        } else {
            courses = courseRepository.findAllByStatusIn(statuses);
        }

        return courses.stream()
                .map(CourseResponse::from)
                .toList();
    }
}
