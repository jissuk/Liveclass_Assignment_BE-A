package com.example.assignment.enrollment.application.usecase;

import com.example.assignment.common.PageResponse;
import com.example.assignment.common.annotaion.UseCase;
import com.example.assignment.enrollment.presentation.dto.EnrollmentResponse;
import com.example.assignment.enrollment.domain.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class GetMyEnrollmentUseCase {

    private final EnrollmentRepository enrollmentRepository;

    @Transactional(readOnly = true)
    public PageResponse<EnrollmentResponse> execute(Long userId, Pageable pageable) {

        Page<EnrollmentResponse> page = enrollmentRepository.findByUserId(userId, pageable)
                                                                .map(EnrollmentResponse::from);

        return PageResponse.from(page);
    }
}
