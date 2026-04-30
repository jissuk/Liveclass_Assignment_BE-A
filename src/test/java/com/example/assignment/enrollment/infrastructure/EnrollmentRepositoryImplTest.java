package com.example.assignment.enrollment.infrastructure;

import com.example.assignment.enrollment.domain.model.Enrollment;
import com.example.assignment.enrollment.exception.EnrollmentNotFoundException;
import com.example.assignment.enrollment.infrastructure.jpa.JpaEnrollmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class EnrollmentRepositoryImplTest {

    @Mock
    private JpaEnrollmentRepository jpaEnrollmentRepository;

    @InjectMocks
    private EnrollmentRepositoryImpl impl;

    @Nested
    @DisplayName("성공 케이스")
    class success{
        @Test
        void 존재하는_ID로_조회하면_Enrollment를_반환한다(){
            // given
            Long enrollmentId = 1L;
            Enrollment enrollment = mock(Enrollment.class);

            given(jpaEnrollmentRepository.findById(enrollmentId))
                    .willReturn(Optional.of(enrollment));

            // when
            Enrollment result = impl.findById(enrollmentId);

            // then
            assertThat(result).isEqualTo(enrollment);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class fail{
        @Test
        void 존재하지_않는_enrollmentId로_조회하면_예외가_발생한다(){
            // given
            Long enrollmentId = 1L;
            given(jpaEnrollmentRepository.findById(enrollmentId))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() ->impl.findById(enrollmentId))
                    .isInstanceOf(EnrollmentNotFoundException.class);
        }
    }
}
