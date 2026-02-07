package com.smlaurindo.risanailstudio.shared.exception;

public class AuthorizationException extends ApiException {

    public AuthorizationException(ErrorCode code) {
        super(ErrorType.AUTHORIZATION_ERROR, code);
    }

    @Override
    public int getHttpStatus() {
        return 403;
    }
}
