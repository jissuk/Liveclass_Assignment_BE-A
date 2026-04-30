package com.example.assignment.course.application.command;

import java.time.LocalDateTime;

public record CourseCommand(
        String name,
        String description,
        Integer price,
        Integer capacity,
        LocalDateTime startDate,
        LocalDateTime endDate
){
}
