package com.example.assignment.enrollment.application.usecase.integration;

import com.example.assignment.course.domain.model.Course;
import com.example.assignment.course.domain.repository.CourseRepository;
import com.example.assignment.enrollment.application.command.EnrollmentCommand;
import com.example.assignment.enrollment.application.usecase.RegisterEnrollmentUseCase;
import com.example.assignment.enrollment.presentation.dto.EnrollmentResponse;
import com.example.assignment.user.domain.model.User;
import com.example.assignment.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@DisplayName("수강신청(Enrollment) 통합(동시성) 테스트")
public class RegisterEnrollmentUseCaseIntegrationTest {

    @Autowired
    private RegisterEnrollmentUseCase useCase;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RedisTemplate<String, Long> redis;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        clearTestDBData();
        initTestDBData();
        clearTestRedisData();
    }

    void clearTestDBData(){
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        jdbcTemplate.execute("TRUNCATE TABLE courses;");
        jdbcTemplate.execute("TRUNCATE TABLE users;");
        jdbcTemplate.execute("TRUNCATE TABLE enrollments;");
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }

    void initTestDBData(){
        Course course = Course.create(
                "course",
                "desc",
                1000,
                10,
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(7),
                LocalDateTime.now().plusDays(14)
        );
        courseRepository.save(course);

        for (int i = 0; i < 20; i++) {
            userRepository.save(User.create("user" + i));
        }
    }

    void clearTestRedisData(){
        redis.getConnectionFactory().getConnection().flushAll();
    }

    @Nested
    @DisplayName("성공 케이스")
    class success{
        @Test
        void 동시에_구매해도_하나만_성공한다() throws Exception {
            // given
            Long userId = 1L;
            Long courseId = 1L;
            EnrollmentCommand command = new EnrollmentCommand(userId, courseId);

            int threadCount = 10;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);

            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch endLatch = new CountDownLatch(threadCount);

            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failCount = new AtomicInteger(0);

            // when
            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        startLatch.await();

                        EnrollmentResponse response = useCase.execute(new EnrollmentCommand(userId, courseId));
                        if(response.isWaitlisted() == false){
                            successCount.incrementAndGet();
                        } else {
                            failCount.incrementAndGet();
                        }
                    } catch (Exception e) {
                        failCount.incrementAndGet();
                    } finally {
                        endLatch.countDown();
                    }
                });
            }

            startLatch.countDown();
            endLatch.await();

            // then
            assertThat(successCount.get()).isEqualTo(1);
            assertThat(failCount.get()).isEqualTo(threadCount - 1);
        }

        @Test
        void 동시에_구매해도_모두_성공한다() throws Exception {
            // given
            Long courseId = 1L;

            int threadCount = 10;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);

            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch endLatch = new CountDownLatch(threadCount);

            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failCount = new AtomicInteger(0);

            // when
            for (int i = 1; i <= threadCount; i++) {
                long userId = i;

                executor.submit(() -> {
                    try {
                        startLatch.await();

                        EnrollmentResponse response = useCase.execute(new EnrollmentCommand(userId, courseId));
                        if(response.isWaitlisted() == false){
                            successCount.incrementAndGet();
                        } else {
                            failCount.incrementAndGet();
                        }

                    } catch (Exception e) {
                        failCount.incrementAndGet();
                    } finally {
                        endLatch.countDown();
                    }
                });
            }

            startLatch.countDown();
            endLatch.await();

            // then
            assertThat(successCount.get()).isEqualTo(threadCount);
            assertThat(failCount.get()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class fail{
        @Test
        @DisplayName("정원이 10명인 강의에 11명이 동시에 신청하면 10명만 성공하고 1명은 실패한다")
        void 정원_초과_동시_신청_테스트() throws Exception {
            // given
            Long courseId = 1L;
            int threadCount = 11;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);

            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch endLatch = new CountDownLatch(threadCount);

            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failCount = new AtomicInteger(0);

            // when
            for (int i = 1; i <= threadCount; i++) {
                long userId = i;
                executor.submit(() -> {
                    try {
                        startLatch.await();

                        EnrollmentResponse response = useCase.execute(new EnrollmentCommand(userId, courseId));
                        if(response.isWaitlisted() == false){
                            successCount.incrementAndGet();
                        } else {
                            failCount.incrementAndGet();
                        }
                    } catch (Exception e) {
                        failCount.incrementAndGet();
                    } finally {
                        endLatch.countDown();
                    }
                });
            }

            startLatch.countDown();
            endLatch.await();

            // then
            assertThat(successCount.get()).isEqualTo(10);
            assertThat(failCount.get()).isEqualTo(1);
        }
    }
}
