package com.smlaurindo.risanailstudio.shared.exception;

import com.smlaurindo.risanailstudio.shared.dto.ApiErrorResponse;
import com.smlaurindo.risanailstudio.shared.filter.RequestIdFilter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${app.env.is-dev:false}")
    private boolean isDev;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception ex) {
        String requestId = getRequestId();

        log.error("Unhandled exception: requestId={}", requestId, ex);

        ApiErrorResponse response = ApiErrorResponse.internal(
                isDev ? ex.getMessage() : null,
                requestId
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private String getRequestId() {
        String requestId = MDC.get(RequestIdFilter.REQUEST_ID_MDC_KEY);
        return requestId != null ? requestId : "unknown";
    }
}
