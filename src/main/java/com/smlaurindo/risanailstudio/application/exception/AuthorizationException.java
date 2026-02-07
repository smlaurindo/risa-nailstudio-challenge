package com.smlaurindo.risanailstudio.application.exception;

public class AuthorizationException extends BaseException {

    public AuthorizationException(ErrorCode code) {
        super(ErrorType.AUTHORIZATION_ERROR, code);
    }

    @Override
    public int getHttpStatus() {
        return 403;
    }
}
