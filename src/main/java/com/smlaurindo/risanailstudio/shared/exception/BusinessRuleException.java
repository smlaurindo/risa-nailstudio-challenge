package com.smlaurindo.risanailstudio.shared.exception;

public class BusinessRuleException extends ApiException {

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
