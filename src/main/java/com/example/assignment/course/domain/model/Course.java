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

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CourseStatus status;

    public void decreaseCapacity() {
        if (this.capacity <= 0) {
            throw new InsufficientCapacityException();
        }
        this.capacity--;
    }

    public void open() {
        this.status = this.status.open();
    }

    public void close() {
        this.status = this.status.close();
    }

    protected Course(String name, String description, Integer price, Integer capacity, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.capacity = capacity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = CourseStatus.DRAFT;
    }

    public static Course create(String name, String description, Integer price, Integer capacity, LocalDateTime startDate, LocalDateTime endDate) {
        return new Course(name, description, price, capacity, startDate, endDate);
    }
}
