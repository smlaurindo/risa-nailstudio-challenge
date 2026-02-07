package com.smlaurindo.risanailstudio.application.exception;

public class BusinessRuleException extends BaseException {

    public BusinessRuleException(ErrorCode code, String field) {
        super(ErrorType.BUSINESS_RULE, code, field);
    }

    public BusinessRuleException(ErrorCode code) {
        super(ErrorType.BUSINESS_RULE, code);
    }

    @Override
    public int getHttpStatus() {
        return 422;
    }
}
