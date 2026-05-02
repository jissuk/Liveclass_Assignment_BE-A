package com.example.assignment.enrollment.domain.model;

import com.example.assignment.enrollment.exception.CancellationPeriodExpiredException;
import com.example.assignment.enrollment.exception.InvalidEnrollmentStateException;
import com.example.assignment.support.fixture.EnrollmentFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("수강 신청(Enrollment) 상태 전이 단위 테스트")
public class EnrollmentTest {

    @Nested
    @DisplayName("성공 케이스")
    class Success {

        @Test
        @DisplayName("PENDING(대기) 상태에서 확정하면 CONFIRMED로 변경된다")
        void 대기상태에서_확정하면_CONFIRMED로_변경된다() {
            // given
            Enrollment enrollment = EnrollmentFixture.givenEnrollment(); // status: PENDING,  반환 가정
            LocalDateTime now = LocalDateTime.now();
            // when
            enrollment.confirm(now);

            // then
            assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.CONFIRMED);
        }

        @Test
        @DisplayName("PENDING(대기) 상태에서 취소하면 CANCELLED로 변경된다")
        void 대기상태에서_취소하면_CANCELLED로_변경된다() {
            // given
            Enrollment enrollment = EnrollmentFixture.givenEnrollment();
            LocalDateTime now = LocalDateTime.now();

            // when
            enrollment.cancel(now);

            // then
            assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.CANCELLED);
        }

        @Test
        @DisplayName("CONFIRMED(확정) 상태에서 취소하면 CANCELLED로 변경된다")
        void 확정상태에서_취소하면_CANCELLED로_변경된다() {
            // given
            Enrollment enrollment = EnrollmentFixture.givenEnrollment();
            LocalDateTime now = LocalDateTime.now();
            enrollment.confirm(now);

            // when
            enrollment.cancel(now);

            // then
            assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.CANCELLED);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class Fail {

        @Test
        @DisplayName("CONFIRMED(확정) 상태에서 다시 확정을 시도하면 예외가 발생한다")
        void 확정상태에서_다시_확정하면_예외가_발생한다() {
            // given
            Enrollment enrollment = EnrollmentFixture.givenEnrollment();
            enrollment.confirm(LocalDateTime.now());

            // when & then
            assertThatThrownBy(()-> enrollment.confirm(LocalDateTime.now()))
                    .isInstanceOf(InvalidEnrollmentStateException.class);
        }

        @Test
        @DisplayName("CANCELLED(취소) 상태에서 확정을 시도하면 예외가 발생한다")
        void 취소상태에서_확정하면_예외가_발생한다() {
            // given
            Enrollment enrollment = EnrollmentFixture.givenEnrollment();
            enrollment.cancel(LocalDateTime.now());

            // when & then
            assertThatThrownBy(()->enrollment.confirm(LocalDateTime.now()))
                    .isInstanceOf(InvalidEnrollmentStateException.class);
        }

        @Test
        @DisplayName("결제 후 7일이 지나면 예외가 발생한다")
        void cancel_fail() {
            // given
            Enrollment enrollment = EnrollmentFixture.givenEnrollment();
            long expireDays = 8L;
            LocalDateTime paymentDateTime = LocalDateTime.now().minusDays(expireDays);
            enrollment.confirm(paymentDateTime);

            // when & then
            assertThatThrownBy(()-> enrollment.cancel(LocalDateTime.now()) )
                    .isInstanceOf(CancellationPeriodExpiredException.class);
        }
    }
}