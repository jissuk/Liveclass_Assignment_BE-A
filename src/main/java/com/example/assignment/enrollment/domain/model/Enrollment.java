package com.example.assignment.enrollment.domain.model;

import com.example.assignment.course.domain.model.Course;
import com.example.assignment.enrollment.exception.CancellationPeriodExpiredException;
import com.example.assignment.user.domain.model.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "enrollments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_enrollments_user_course",
                        columnNames = {"user_id", "course_id"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Enrollment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "payment_date_time")
    private LocalDateTime paymentDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    public void confirm(LocalDateTime now) {
        this.status = this.status.confirm();
        this.paymentDateTime = now;
    }

    public void cancel(LocalDateTime now) {
        if (this.status == EnrollmentStatus.CONFIRMED) {
            if (isCancellationPeriodExpired(now)) {
                throw new CancellationPeriodExpiredException();
            }
        }
        this.status = EnrollmentStatus.CANCELLED;
    }

    private boolean isCancellationPeriodExpired(LocalDateTime now) {
        return now.isAfter(paymentDateTime.plusDays(7));
    }

    protected Enrollment(User user, Course course) {
        this.user = user;
        this.course = course;
        this.status = EnrollmentStatus.PENDING;
        this.createdAt = LocalDate.now();
    }

    public static Enrollment create(User user, Course course) {
        return new Enrollment(user, course);
    }
}
