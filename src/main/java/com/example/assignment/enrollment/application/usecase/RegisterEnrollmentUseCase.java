package com.example.assignment.enrollment.application.usecase;

import com.example.assignment.common.annotaion.UseCase;
import com.example.assignment.course.domain.model.Course;
import com.example.assignment.course.domain.repository.CourseRepository;
import com.example.assignment.enrollment.application.command.EnrollmentCommand;
import com.example.assignment.enrollment.application.port.out.EnrollmentQueuePort;
import com.example.assignment.enrollment.domain.model.Enrollment;
import com.example.assignment.enrollment.domain.repository.EnrollmentRepository;
import com.example.assignment.enrollment.presentation.dto.EnrollmentResponse;

import com.example.assignment.user.domain.model.User;
import com.example.assignment.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionTemplate;


@UseCase
@RequiredArgsConstructor
@Slf4j
public class RegisterEnrollmentUseCase {

    private final EnrollmentQueuePort enrollmentQueuePort;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    private final TransactionTemplate transactionTemplate;

    public EnrollmentResponse execute(EnrollmentCommand command) {
        Long courseId = command.courseId();
        Long userId = command.userId();

        enrollmentQueuePort.add(courseId, userId, System.currentTimeMillis());

        Long rank = enrollmentQueuePort.getRank(courseId, userId);
        int capacity = courseRepository.getCapacity(courseId);

        if (rank < capacity) {
            EnrollmentResponse response = transactionTemplate.execute(status -> {
                courseRepository.decreaseCapacity(courseId);
                return createEnrollment(command);
            });
            enrollmentQueuePort.remove(courseId, userId);
            return response;
        }

        long waitNumber = rank - capacity + 1;
        return EnrollmentResponse.waiting(waitNumber);
    }

    private EnrollmentResponse createEnrollment(EnrollmentCommand command) {
        User user = userRepository.findById(command.userId());
        Course course = courseRepository.findById(command.courseId());
        Enrollment enrollment = enrollmentRepository.save(Enrollment.create(user, course));

        return EnrollmentResponse.from(enrollment);
    }
}