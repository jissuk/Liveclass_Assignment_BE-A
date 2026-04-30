package com.example.assignment.course.presentation.dto;

import com.example.assignment.course.domain.model.Course;
import lombok.Builder;

import java.time.LocalDateTime;

public record CourseResponse(
        String name,
        String description,
        Integer price,
        Integer capacity,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
    public static CourseResponse from(Course course) {
        return new CourseResponse(
                course.getName(),
                course.getDescription(),
                course.getPrice(),
                course.getCapacity(),
                course.getStartDate(),
                course.getEndDate()
        );
    }
}