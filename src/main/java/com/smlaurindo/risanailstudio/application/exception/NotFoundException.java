package com.smlaurindo.risanailstudio.application.exception;

public class NotFoundException extends BaseException {
    public NotFoundException(ErrorCode code) {
        super(ErrorType.NOT_FOUND, code);
    }

    @Override
    public int getHttpStatus() {
        return 404;
    }
}
