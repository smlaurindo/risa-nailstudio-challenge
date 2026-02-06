package com.smlaurindo.risanailstudio.shared.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public abstract class ApiException extends RuntimeException {

    private final ErrorType type;
    private final ErrorCode code;
    private final String field;
    private final Map<String, Object> meta;

    protected ApiException(ErrorType type, ErrorCode code) {
        super(code.name());
        this.type = type;
        this.code = code;
        this.field = null;
        this.meta = null;
    }

    protected ApiException(ErrorType type, ErrorCode code, String field) {
        super(code.name());
        this.type = type;
        this.code = code;
        this.field = field;
        this.meta = null;
    }

    protected ApiException(ErrorType type, ErrorCode code, Map<String, Object> meta) {
        super(code.name());
        this.type = type;
        this.code = code;
        this.field = null;
        this.meta = meta;
    }

    protected ApiException(ErrorType type, ErrorCode code, String field, Map<String, Object> meta) {
        super(code.name());
        this.type = type;
        this.code = code;
        this.field = field;
        this.meta = meta;
    }

    protected ApiException(ErrorType type, ErrorCode code, Throwable cause) {
        super(code.name(), cause);
        this.type = type;
        this.code = code;
        this.field = null;
        this.meta = null;
    }

    protected ApiException(ErrorType type, ErrorCode code, String field, Throwable cause) {
        super(code.name(), cause);
        this.type = type;
        this.code = code;
        this.field = field;
        this.meta = null;
    }

    protected ApiException(ErrorType type, ErrorCode code, Map<String, Object> meta, Throwable cause) {
        super(code.name(), cause);
        this.type = type;
        this.code = code;
        this.field = null;
        this.meta = meta;
    }

    /**
     * Retorna o status HTTP correspondente a esta exceção.
     * Cada subclasse deve implementar.
     */
    public abstract int getHttpStatus();
}

