package com.example.assignment.common.exception;

import com.example.assignment.course.exception.CourseNotFoundException;
import com.example.assignment.course.exception.InvalidCourseStateException;
import com.example.assignment.enrollment.exception.AlreadyEnrollmentException;
import com.example.assignment.enrollment.exception.EnrollmentNotFoundException;
import com.example.assignment.enrollment.exception.InsufficientCapacityException;
import com.example.assignment.enrollment.exception.InvalidEnrollmentStateException;
import com.example.assignment.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCourseNotFound(CourseNotFoundException e){

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("CourseNotFound", e.getMessage()));
    }

    @ExceptionHandler(InvalidCourseStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCourseState(InvalidCourseStateException e){

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("InvalidCourseState", e.getMessage()));
    }

    @ExceptionHandler(AlreadyEnrollmentException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyEnrollment(AlreadyEnrollmentException e){

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("AlreadyEnrollment", e.getMessage()));
    }

    @ExceptionHandler(EnrollmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEnrollmentNotFound(EnrollmentNotFoundException e){

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("EnrollmentNotFound", e.getMessage()));
    }

    @ExceptionHandler(InsufficientCapacityException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientCapacity(InsufficientCapacityException e){

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("InsufficientCapacity", e.getMessage()));
    }

    @ExceptionHandler(InvalidEnrollmentStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEnrollmentState(InvalidEnrollmentStateException e){

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("InvalidEnrollmentState", e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e){

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("UserNotFound", e.getMessage()));
    }
}
