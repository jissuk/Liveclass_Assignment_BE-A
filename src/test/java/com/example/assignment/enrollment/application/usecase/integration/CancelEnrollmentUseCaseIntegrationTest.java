package com.example.assignment.enrollment.application.usecase.integration;

import com.example.assignment.course.domain.model.Course;
import com.example.assignment.course.domain.repository.CourseRepository;
import com.example.assignment.enrollment.application.usecase.CancelEnrollmentUseCase;
import com.example.assignment.enrollment.domain.model.Enrollment;
import com.example.assignment.enrollment.domain.model.EnrollmentStatus;
import com.example.assignment.enrollment.domain.repository.EnrollmentRepository;
import com.example.assignment.user.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@DisplayName("수강신청(Enrollment) 취소 통합(DB) 테스트")
public class CancelEnrollmentUseCaseIntegrationTest {

    @Autowired
    private CancelEnrollmentUseCase cancelEnrollmentUseCase;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void 취소시_DB에_상태변경과_정원증가가_반영된다() {
        // given
        Course course = Course.create(
                "course",
                "desc",
                1000,
                0,
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(7),
                LocalDateTime.now().plusDays(14)
        );
        User user = User.create("user");
        Enrollment enrollment = Enrollment.create(user, course);
        enrollment.confirm(LocalDateTime.now());

        Course saveCourse = courseRepository.save(course);
        Enrollment saveEnrollment = enrollmentRepository.save(enrollment);

        // when
        cancelEnrollmentUseCase.execute(saveEnrollment.getId());

        // then
        Enrollment result = enrollmentRepository.findById(saveEnrollment.getId());
        Course updatedCourse = courseRepository.findById(saveCourse.getId());

        assertThat(result.getStatus()).isEqualTo(EnrollmentStatus.CANCELLED);
        assertThat(updatedCourse.getCapacity()).isEqualTo(1);
    }
}
