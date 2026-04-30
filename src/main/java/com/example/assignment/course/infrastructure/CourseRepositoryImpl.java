package com.example.assignment.course.infrastructure;

import com.example.assignment.course.domain.model.Course;
import com.example.assignment.course.domain.model.CourseStatus;
import com.example.assignment.course.domain.repository.CourseRepository;
import com.example.assignment.course.exception.CourseNotFoundException;
import com.example.assignment.course.infrastructure.jpa.JpaCourseRepository;
import com.example.assignment.enrollment.exception.InsufficientCapacityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CourseRepositoryImpl implements CourseRepository {

    private final JpaCourseRepository jpaCourseRepository;

    @Override
    public Course findById(Long courseId) {
        return jpaCourseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);
    }

    @Override
    public List<Course> findAll() {
        return jpaCourseRepository.findAll();
    }

    @Override
    public List<Course> findAllByStatusIn(List<CourseStatus> statuses) {
        return jpaCourseRepository.findAllByStatusIn(statuses);
    }

    @Override
    public int getCapacity(Long courseId) {
        return jpaCourseRepository.getCapacity(courseId);
    }

    @Override
    public Course save(Course course) {
        return jpaCourseRepository.save(course);
    }

    @Override
    public int decreaseCapacity(Long courseId) {
        int updated = jpaCourseRepository.decreaseCapacity(courseId);
        if (updated == 0) {
            throw new InsufficientCapacityException();
        }

        return updated;
    }
}
