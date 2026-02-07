package com.smlaurindo.risanailstudio.shared.exception;

public class NotFoundException extends ApiException {
    public NotFoundException(ErrorCode code) {
        super(ErrorType.NOT_FOUND, code);
    }

    @Override
    public int getHttpStatus() {
        return 404;
    }
}
