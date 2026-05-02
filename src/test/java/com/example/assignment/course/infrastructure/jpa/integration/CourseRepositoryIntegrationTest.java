package com.example.assignment.course.infrastructure.jpa.integration;

import com.example.assignment.course.domain.model.Course;
import com.example.assignment.course.domain.repository.CourseRepository;
import com.example.assignment.enrollment.exception.InsufficientCapacityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@DisplayName("강의(Course) 통합(DB) 테스트")
public class CourseRepositoryIntegrationTest {

    @Autowired
    private CourseRepository courseRepository;

    @Nested
    @DisplayName("성공 케이스")
    class success{

        @Test
        void capacity가_정상적으로_감소한다() {
            // given
            Course course = Course.create(
                    "course",
                    "desc",
                    1000,
                    10,
                    LocalDateTime.now().plusDays(3),
                    LocalDateTime.now().plusDays(7),
                    LocalDateTime.now().plusDays(14)
            );
            Course save = courseRepository.save(course);

            // when
            int result = courseRepository.decreaseCapacity(save.getId());
            Course updated = courseRepository.findById(save.getId());

            // then
            assertThat(result).isEqualTo(1);
            assertThat(updated.getCapacity()).isEqualTo(9);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class fail{
        @Test
        void 잔여_정원이_0인_경우_정원을_감소시킬_수_없다() {
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
            Course save = courseRepository.save(course);

            // when & then
            assertThatThrownBy(()-> courseRepository.decreaseCapacity(save.getId()))
                    .isInstanceOf(InsufficientCapacityException.class);
        }
    }
}
