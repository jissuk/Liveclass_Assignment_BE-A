package com.example.assignment.course.infrastructure.domain.model;

import com.example.assignment.course.domain.model.Course;
import com.example.assignment.course.domain.model.CourseStatus;
import com.example.assignment.course.exception.InvalidCourseStateException;
import com.example.assignment.support.fixture.CourseFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("강의(Course) 상태 전이 단위 테스트")
public class CourseTest {

    @Nested
    @DisplayName("성공 케이스")
    class Success {

        @Test
        @DisplayName("DRAFT(초안) 상태에서 open을 호출하면 OPEN(모집 중)으로 변경된다")
        void DRAFT_상태에서_open_호출_시_OPEN으로_변경() {
            // given
            Course course = CourseFixture.givenCourse();

            // when
            course.open();

            // then
            assertThat(course.getStatus()).isEqualTo(CourseStatus.OPEN);
        }

        @Test
        @DisplayName("OPEN(모집 중) 상태에서 close를 호출하면 CLOSED(모집 마감)로 변경된다")
        void OPEN_상태에서_close_호출_시_CLOSED로_변경() {
            // given
            Course course = CourseFixture.givenCourse();
            course.open();

            // when
            course.close();

            // then
            assertThat(course.getStatus()).isEqualTo(CourseStatus.CLOSED);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class Fail {

        @Test
        @DisplayName("DRAFT(초안) 상태에서 바로 close를 호출하면 예외가 발생한다")
        void DRAFT_상태에서_바로_close_호출_시_예외_발생() {
            // given
            Course course = CourseFixture.givenCourse();

            // when & then
            assertThatThrownBy(course::close)
                    .isInstanceOf(InvalidCourseStateException.class)
                    .hasMessageContaining("DRAFT")
                    .hasMessageContaining("CLOSE");
        }

        @Test
        @DisplayName("OPEN(모집 중) 상태에서 다시 open을 호출하면 예외가 발생한다")
        void OPEN_상태에서_다시_open_호출_시_예외_발생() {
            // given
            Course course = CourseFixture.givenCourse();
            course.open();

            // when & then
            assertThatThrownBy(course::open)
                    .isInstanceOf(InvalidCourseStateException.class);
        }

        @Test
        @DisplayName("CLOSED(모집 마감) 상태에서는 open이나 close 모두 예외가 발생한다")
        void CLOSED_상태에서_상태_변경_시도_시_예외_발생() {
            // given
            Course course = CourseFixture.givenCourse();
            course.open();
            course.close(); // DRAFT -> OPEN -> CLOSED

            // when & then
            assertThatThrownBy(course::open)
                    .isInstanceOf(InvalidCourseStateException.class);

            assertThatThrownBy(course::close)
                    .isInstanceOf(InvalidCourseStateException.class);
        }
    }
}