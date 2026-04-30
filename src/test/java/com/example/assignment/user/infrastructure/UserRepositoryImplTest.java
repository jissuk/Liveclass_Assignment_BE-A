package com.example.assignment.user.infrastructure;

import com.example.assignment.user.domain.model.User;
import com.example.assignment.user.exception.UserNotFoundException;
import com.example.assignment.user.infrastructure.jpa.JpaUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryImplTest {

    @Mock
    private JpaUserRepository jpaUserRepository;

    @InjectMocks
    private UserRepositoryImpl impl;

    @Nested
    @DisplayName("성공 케이스")
    class success{
        @Test
        void 존재하는_ID로_조회하면_User를_반환한다() {
            // gvien
            Long userId = 1L;
            User user = mock(User.class);
            given(jpaUserRepository.findById(userId)).willReturn(Optional.of(user));

            // when
            User result = impl.findById(userId);

            // then
            assertThat(result).isEqualTo(user);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class fail{
        @Test
        void 존재하지_않는_userId로_조회하면_예외를_발생한다() {
            // given
            Long userId = 1L;
            given(jpaUserRepository.findById(userId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> impl.findById(userId))
                    .isInstanceOf(UserNotFoundException.class);
        }
    }
}
