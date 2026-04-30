package com.example.assignment.common.exception;

public record ErrorResponse(
        String code,
        String message
) {
}
