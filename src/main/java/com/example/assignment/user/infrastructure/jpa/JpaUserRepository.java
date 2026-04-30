package com.example.assignment.user.infrastructure.jpa;

import com.example.assignment.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<User, Long> {
}
