package com.example.assignment.user.exception;

public class UserNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "유저 조회에 실패하였습니다.";

    public UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
