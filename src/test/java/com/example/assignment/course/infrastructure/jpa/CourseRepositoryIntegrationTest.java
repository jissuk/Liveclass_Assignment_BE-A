package com.example.assignment.course.infrastructure.jpa;

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
                    "name",
                    "description",
                    30_000,
                    10,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(1)
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
        @DisplayName("잔여 정원이 0인 경우 정원을 감소시킬 수 없다")
        void decreaseCapacity_fail_when_zero_capacity() {
            // given
            Course course = Course.create(
                    "course",
                    "desc",
                    1000,
                    0,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(1)
            );
            Course save = courseRepository.save(course);

            // when & then
            assertThatThrownBy(()-> courseRepository.decreaseCapacity(save.getId()))
                    .isInstanceOf(InsufficientCapacityException.class);
        }
    }
}
