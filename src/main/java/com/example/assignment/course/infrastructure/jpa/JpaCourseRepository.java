package com.example.assignment.course.infrastructure.jpa;

import com.example.assignment.course.domain.model.Course;
import com.example.assignment.course.domain.model.CourseStatus;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaCourseRepository extends JpaRepository<Course,Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE Course c
        SET c.capacity = c.capacity - 1
        WHERE c.id = :id
        AND c.capacity > 0
    """)
    int decreaseCapacity(Long id);

    @Query("SELECT c FROM Course c WHERE c.status IN :statuses")
    List<Course> findAllByStatusIn(@Param("statuses") List<CourseStatus> statuses);

    @Query("SELECT c.capacity FROM Course c WHERE c.id = :courseId")
    int getCapacity(Long courseId);
}
