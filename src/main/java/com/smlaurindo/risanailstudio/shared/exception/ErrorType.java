package com.smlaurindo.risanailstudio.shared.exception;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorType {
    VALIDATION_ERROR("validation_error"),
    CONFLICT("conflict"),
    NOT_FOUND("not_found"),
    BUSINESS_RULE("business_rule"),
    INTERNAL_ERROR("internal_error");

    private final String value;

    ErrorType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
