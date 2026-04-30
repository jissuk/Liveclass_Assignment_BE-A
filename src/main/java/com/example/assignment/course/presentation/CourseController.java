package com.example.assignment.course.presentation;

import com.example.assignment.course.application.usecae.GetCourseListUseCase;
import com.example.assignment.course.application.usecae.GetCourseUseCase;
import com.example.assignment.course.application.usecae.RegisterCourseUseCase;
import com.example.assignment.course.application.command.CourseCommand;
import com.example.assignment.course.domain.model.CourseStatus;
import com.example.assignment.course.presentation.dto.CourseRequest;
import com.example.assignment.course.presentation.dto.CourseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {

    private final GetCourseUseCase getCourseUsecase;
    private final GetCourseListUseCase getCourseListUseCase;
    private final RegisterCourseUseCase registerCourseUseCase;

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseResponse> getCourseDetail(@PathVariable Long courseId) {

        return ResponseEntity
                .ok()
                .body(getCourseUsecase.execute(courseId));
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseResponse>> getCourseList(@RequestParam(required = false) List<CourseStatus> statuses){

        return ResponseEntity
                .ok()
                .body(getCourseListUseCase.execute(statuses));
    }

    @PostMapping
    public ResponseEntity<CourseResponse> registerCourse(@RequestBody CourseRequest request){
        CourseCommand command = request.toCommand();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registerCourseUseCase.execute(command));
    }
}
