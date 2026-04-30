package com.example.assignment.user.domain.repository;

import com.example.assignment.user.domain.model.User;

public interface UserRepository {
    User findById(Long aLong);
    User save(User user);
}
