package com.example.assignment.course.domain.model;

import com.example.assignment.enrollment.exception.InsufficientCapacityException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "courses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "enrollment_deadline")
    private LocalDateTime enrollmentDeadline;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;


    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CourseStatus status;

    public void open() {
        this.status = this.status.open();
    }

    public void close() {
        this.status = this.status.close();
    }

    protected Course(String name, String description, Integer price, Integer capacity, LocalDateTime enrollmentDeadline, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.capacity = capacity;
        this.enrollmentDeadline = enrollmentDeadline;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = CourseStatus.DRAFT;
    }

    public static Course create(String name, String description, Integer price, Integer capacity, LocalDateTime enrollmentDeadline, LocalDateTime startDate, LocalDateTime endDate) {
        return new Course(name, description, price, capacity, enrollmentDeadline, startDate, endDate);
    }
}
