package com.example.assignment.user.infrastructure;

import com.example.assignment.user.domain.model.User;
import com.example.assignment.user.domain.repository.UserRepository;
import com.example.assignment.user.exception.UserNotFoundException;
import com.example.assignment.user.infrastructure.jpa.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public User findById(Long aLong) {
        return jpaUserRepository.findById(aLong)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User save(User user) {
        return jpaUserRepository.save(user);
    }
}
