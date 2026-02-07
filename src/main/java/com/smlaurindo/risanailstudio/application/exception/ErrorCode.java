package com.smlaurindo.risanailstudio.application.exception;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCode {
    USER_ALREADY_EXISTS,
    INVALID_CREDENTIALS,
    INVALID_TOKEN,
    EXPIRED_TOKEN,
    TOKEN_REVOKED,
    INSUFFICIENT_PRIVILEGES,
    CUSTOMER_NOT_FOUND,
    SERVICE_NOT_FOUND,
    APPOINTMENT_NOT_FOUND,
    CUSTOMER_NAME_REQUIRED,
    APPOINTMENT_SLOT_UNAVAILABLE,
    APPOINTMENT_ALREADY_CONFIRMED,
    APPOINTMENT_ALREADY_CANCELLED,
    APPOINTMENT_IN_THE_PAST,
    APPOINTMENT_SLOT_INVALID,
    DATE_RANGE_INVALID,
    DATE_RANGE_TOO_LONG,
    INTERNAL_ERROR;

    @JsonValue
    public String getValue() {
        return name();
    }

    public enum Validation {
        REQUIRED,
        TOO_SHORT,
        TOO_LONG,
        OUT_OF_RANGE,
        INVALID_FORMAT,
        INVALID_EMAIL,
        INVALID_URL,
        INVALID_DATE,
        INVALID_UUID,
        PASSWORD_TOO_WEAK,
        PASSWORD_MISMATCH;

        @JsonValue
        public String getValue() {
            return name();
        }
    }
}
