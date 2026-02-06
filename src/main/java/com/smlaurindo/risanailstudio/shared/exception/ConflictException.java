package com.smlaurindo.risanailstudio.shared.exception;

import java.util.Map;

public class ConflictException extends ApiException {

    public ConflictException(ErrorCode code) {
        super(ErrorType.CONFLICT, code);
    }

    public ConflictException(ErrorCode code, String field) {
        super(ErrorType.CONFLICT, code, field);
    }

    public ConflictException(ErrorCode code, Throwable cause) {
        super(ErrorType.CONFLICT, code, cause);
    }

    public ConflictException(ErrorCode code, String field, Throwable cause) {
        super(ErrorType.CONFLICT, code, field, cause);
    }

    public ConflictException(ErrorCode code, Map<String, Object> meta) {
        super(ErrorType.CONFLICT, code, meta);
    }

    @Override
    public int getHttpStatus() {
        return 409;
    }
}