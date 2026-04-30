package com.example.assignment.course.presentation.dto;

import com.example.assignment.course.application.command.CourseCommand;

import java.time.LocalDateTime;

public record CourseRequest(
        String name,
        String description,
        Integer price,
        Integer capacity,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
    public CourseCommand toCommand(){
        return new CourseCommand(name, description, price, capacity, startDate, endDate);
    }
}