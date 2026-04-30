package com.example.assignment.enrollment.domain.model;

import com.example.assignment.enrollment.exception.InvalidEnrollmentStateException;

public enum EnrollmentStatus {

    PENDING {
        @Override
        public EnrollmentStatus confirm() {
            return CONFIRMED;
        }

        @Override
        public EnrollmentStatus cancel() {
            return CANCELLED;
        }
    },

    CONFIRMED {
        @Override
        public EnrollmentStatus cancel() {
            return CANCELLED;
        }
    },

    CANCELLED;

    public EnrollmentStatus confirm() {
        throw new InvalidEnrollmentStateException(this, EnrollmentAction.CONFIRM);
    }

    public EnrollmentStatus cancel() {
        throw new InvalidEnrollmentStateException(this, EnrollmentAction.CANCEL);
    }
}