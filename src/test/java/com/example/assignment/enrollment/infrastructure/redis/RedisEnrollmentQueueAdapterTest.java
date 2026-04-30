package com.example.assignment.enrollment.infrastructure.redis;

import com.example.assignment.TestcontainersConfiguration;
import com.example.assignment.enrollment.application.port.out.EnrollmentQueuePort;
import com.example.assignment.enrollment.exception.AlreadyEnrollmentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
public class RedisEnrollmentQueueAdapterTest {

    @Autowired
    private EnrollmentQueuePort port;

    @Autowired
    private RedisTemplate<String, String> redis;

    @BeforeEach
    void clearRedis() {
        redis.getConnectionFactory().getConnection().flushAll();
    }

    @Nested
    @DisplayName("성공 케이스")
    class success {

        @Test
        void 처음_신청한_사용자는_대기열_등록에_성공한다() {
            // given
            Long courseId = 1L;
            Long userId = 100L;
            long requestTime = System.currentTimeMillis();

            // when
            port.add(courseId, userId, requestTime);
            Long rank = port.getRank(courseId, userId);

            // then
            assertThat(rank).isEqualTo(0L);
        }

        @Test
        void 먼저_신청한_사용자가_더_앞선_순위를_받는다() {
            // given
            Long courseId = 1L;
            Long userIdA = 100L;
            Long userIdB = 200L;
            long requestTimeA = System.currentTimeMillis();
            long requestTimeB = System.currentTimeMillis() +1000;

            // when
            port.add(courseId, userIdA, requestTimeA);
            port.add(courseId, userIdB, requestTimeB);

            // then
            assertThat(port.getRank(courseId, userIdA)).isEqualTo(0L);
            assertThat(port.getRank(courseId, userIdB)).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class fail {

        @Test
        void 이미_대기열에_존재하는_사용자가_재신청하면_예외가_발생한다() {
            // given
            Long courseId = 1L;
            Long userId = 100L;
            long requestTime = System.currentTimeMillis();

            // given
            port.add(courseId, userId, requestTime);

            // when & then
            assertThatThrownBy(() -> port.add(courseId, userId, requestTime + 1000))
                    .isInstanceOf(AlreadyEnrollmentException.class);
        }

        @Test
        void 대기열에_없는_사용자의_순위를_조회하면_null을_반환한다() {
            // given
            Long courseId = 1L;
            Long userId = 100L;

            // when
            Long rank = port.getRank(courseId, userId);

            // then
            assertThat(rank).isNull();
        }
    }
}