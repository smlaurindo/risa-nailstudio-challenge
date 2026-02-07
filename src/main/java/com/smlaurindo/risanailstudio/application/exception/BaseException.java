package com.smlaurindo.risanailstudio.application.exception;

import java.util.Map;

public abstract class BaseException extends RuntimeException {

    private final ErrorType type;
    private final ErrorCode code;
    private final String field;
    private final Map<String, Object> meta;

    protected BaseException(ErrorType type, ErrorCode code) {
        super(code.name());
        this.type = type;
        this.code = code;
        this.field = null;
        this.meta = null;
    }

    protected BaseException(ErrorType type, ErrorCode code, String field) {
        super(code.name());
        this.type = type;
        this.code = code;
        this.field = field;
        this.meta = null;
    }

    protected BaseException(ErrorType type, ErrorCode code, Map<String, Object> meta) {
        super(code.name());
        this.type = type;
        this.code = code;
        this.field = null;
        this.meta = meta;
    }

    protected BaseException(ErrorType type, ErrorCode code, String field, Map<String, Object> meta) {
        super(code.name());
        this.type = type;
        this.code = code;
        this.field = field;
        this.meta = meta;
    }

    protected BaseException(ErrorType type, ErrorCode code, Throwable cause) {
        super(code.name(), cause);
        this.type = type;
        this.code = code;
        this.field = null;
        this.meta = null;
    }

    protected BaseException(ErrorType type, ErrorCode code, String field, Throwable cause) {
        super(code.name(), cause);
        this.type = type;
        this.code = code;
        this.field = field;
        this.meta = null;
    }

    protected BaseException(ErrorType type, ErrorCode code, Map<String, Object> meta, Throwable cause) {
        super(code.name(), cause);
        this.type = type;
        this.code = code;
        this.field = null;
        this.meta = meta;
    }

    public abstract int getHttpStatus();

    public ErrorType getType() {
        return this.type;
    }

    public ErrorCode getCode() {
        return this.code;
    }

    public String getField() {
        return this.field;
    }

    public Map<String, Object> getMeta() {
        return this.meta;
    }
}

