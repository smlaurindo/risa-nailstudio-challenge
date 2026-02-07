package com.smlaurindo.risanailstudio.shared.exception;

import java.util.Map;

public class AuthenticationException extends ApiException {

    public AuthenticationException(ErrorCode code) {
        super(ErrorType.AUTHENTICATION_ERROR, code);
    }

    @Override
    public int getHttpStatus() {
        return 401;
    }
}
