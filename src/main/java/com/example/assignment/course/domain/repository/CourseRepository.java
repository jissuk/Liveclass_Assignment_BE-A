package com.example.assignment.course.domain.repository;

import com.example.assignment.course.domain.model.Course;
import com.example.assignment.course.domain.model.CourseStatus;

import java.util.List;

public interface CourseRepository {
    Course findById(Long courseId);
    Course save(Course course);
    int decreaseCapacity(Long courseId);
    List<Course> findAll();
    List<Course> findAllByStatusIn(List<CourseStatus> statuses);
    int getCapacity(Long courseId);
}
