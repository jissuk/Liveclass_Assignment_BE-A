package com.example.assignment.course.application.usecae;

import com.example.assignment.common.annotaion.UseCase;
import com.example.assignment.course.application.command.CourseCommand;
import com.example.assignment.course.application.mapper.CourseMapper;
import com.example.assignment.course.domain.model.Course;
import com.example.assignment.course.domain.repository.CourseRepository;
import com.example.assignment.course.presentation.dto.CourseResponse;
import com.example.assignment.enrollment.redis.EnrollmentRedisKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.time.LocalDateTime;

@UseCase
@RequiredArgsConstructor
public class RegisterCourseUseCase {

    private final CourseRepository courseRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public CourseResponse execute(CourseCommand command) {
        Course course = CourseMapper.toDomain(command);
        Course result = courseRepository.save(course);

        Duration duration = Duration.between(LocalDateTime.now(), result.getEndDate());
        if (!duration.isNegative() && !duration.isZero()) {
            String queueKey = EnrollmentRedisKeys.getQueueKey(result.getId());
            redisTemplate.opsForValue().set(queueKey, "INIT", duration);
        }

        return CourseResponse.from(result);
    }
}
