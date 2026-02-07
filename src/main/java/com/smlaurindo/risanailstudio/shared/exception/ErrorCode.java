package com.smlaurindo.risanailstudio.shared.exception;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCode {
    USER_ALREADY_EXISTS,
    INVALID_CREDENTIALS,
    INSUFFICIENT_PRIVILEGES,
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
