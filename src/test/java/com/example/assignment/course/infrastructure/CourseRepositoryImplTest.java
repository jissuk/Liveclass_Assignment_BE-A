package com.example.assignment.course.infrastructure;

import com.example.assignment.course.domain.model.Course;
import com.example.assignment.course.exception.CourseNotFoundException;
import com.example.assignment.course.infrastructure.jpa.JpaCourseRepository;
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
public class CourseRepositoryImplTest {

    @Mock
    private JpaCourseRepository jpaCourseRepository;

    @InjectMocks
    private CourseRepositoryImpl impl;

    @Nested
    @DisplayName("성공 케이스")
    class success{
        @Test
        void 존재하는_ID로_조회하면_Course를_반환한다(){
            // given
            Long courseId = 1L;
            Course course = mock(Course.class);
            given(jpaCourseRepository.findById(courseId))
                    .willReturn(Optional.of(course));

            // when
            Course result = impl.findById(courseId);

            // then
            assertThat(result).isEqualTo(course);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class fail {
        @Test
        void 존재하지_않는_courseId로_조회하면_예외가_발생한다() {
            // given
            Long courseId = 1L;
            given(jpaCourseRepository.findById(courseId))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> impl.findById(courseId))
                    .isInstanceOf(CourseNotFoundException.class);
        }
    }
}
