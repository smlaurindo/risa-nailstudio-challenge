package com.smlaurindo.risanailstudio.application.exception;

public class ConflictException extends BaseException {

    public ConflictException(ErrorCode code, String field) {
        super(ErrorType.CONFLICT, code, field);
    }

    @Override
    public int getHttpStatus() {
        return 409;
    }
}