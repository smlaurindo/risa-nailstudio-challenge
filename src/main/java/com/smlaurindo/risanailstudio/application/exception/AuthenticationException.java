package com.smlaurindo.risanailstudio.application.exception;

public class AuthenticationException extends BaseException {

    public AuthenticationException(ErrorCode code) {
        super(ErrorType.AUTHENTICATION_ERROR, code);
    }

    @Override
    public int getHttpStatus() {
        return 401;
    }
}
