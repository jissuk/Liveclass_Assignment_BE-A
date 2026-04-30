package com.example.assignment.enrollment.presentation;

import com.example.assignment.common.PageResponse;
import com.example.assignment.enrollment.application.command.EnrollmentCommand;
import com.example.assignment.enrollment.application.usecase.ConfirmEnrollmentUseCase;
import com.example.assignment.enrollment.presentation.dto.EnrollmentRequest;
import com.example.assignment.enrollment.application.usecase.CancelEnrollmentUseCase;
import com.example.assignment.enrollment.application.usecase.RegisterEnrollmentUseCase;
import com.example.assignment.enrollment.application.usecase.GetMyEnrollmentUseCase;
import com.example.assignment.enrollment.presentation.dto.EnrollmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final GetMyEnrollmentUseCase getMyEnrollmentUseCase;
    private final RegisterEnrollmentUseCase registerEnrollmentUseCase;
    private final ConfirmEnrollmentUseCase confirmEnrollmentUseCase;
    private final CancelEnrollmentUseCase cancelEnrollmentUseCase;

    @GetMapping("/{userId}")
    public ResponseEntity<PageResponse<EnrollmentResponse>> getMyEnrollment(@PathVariable Long userId, Pageable pageable) {

        return ResponseEntity
                .ok()
                .body(getMyEnrollmentUseCase.execute(userId, pageable));
    }

    @PostMapping
    public ResponseEntity<EnrollmentResponse> registerEnrollment(@RequestBody EnrollmentRequest request) {

        EnrollmentCommand command = request.toCommand();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registerEnrollmentUseCase.execute(command));
    }

    @PatchMapping("/{enrollmentId}/confirm")
    public ResponseEntity<Void> confirmEnrollment(@PathVariable Long enrollmentId) {
        confirmEnrollmentUseCase.execute(enrollmentId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{enrollmentId}/cancel")
    public ResponseEntity<Void> cancelEnrollment(@PathVariable Long enrollmentId) {
        cancelEnrollmentUseCase.execute(enrollmentId);

        return ResponseEntity.noContent().build();
    }
}
