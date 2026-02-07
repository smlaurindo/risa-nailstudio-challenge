package com.smlaurindo.risanailstudio.shared.exception;

public class ConflictException extends ApiException {

    public ConflictException(ErrorCode code, String field) {
        super(ErrorType.CONFLICT, code, field);
    }

    @Override
    public int getHttpStatus() {
        return 409;
    }
}