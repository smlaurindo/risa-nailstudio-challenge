package com.smlaurindo.risanailstudio.shared.exception;

import com.smlaurindo.risanailstudio.shared.dto.ApiErrorResponse;
import com.smlaurindo.risanailstudio.shared.filter.RequestIdFilter;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Pattern ARRAY_INDEX_PATTERN = Pattern.compile("\\[(\\d+)]");

    @Value("${app.env.is-dev:false}")
    private boolean isDev;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String requestId = getRequestId();

        List<ApiErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ApiErrorResponse.FieldError(
                        mapValidationCode(error.getCode(), error.getField(), error.getRejectedValue()),
                        normalizeFieldPath(error.getField())
                ))
                .toList();

        log.warn("Bean validation failed: errors={}, requestId={}", fieldErrors.size(), requestId);

        ApiErrorResponse response = new ApiErrorResponse(
                new ApiErrorResponse.ErrorBody(
                        ErrorType.VALIDATION_ERROR,
                        null,
                        null,
                        fieldErrors,
                        null,
                        isDev ? ex.getMessage() : null,
                        requestId
                ),
                400
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        String requestId = getRequestId();

        List<ApiErrorResponse.FieldError> fieldErrors = ex.getConstraintViolations().stream()
                .map(violation -> new ApiErrorResponse.FieldError(
                        mapConstraintCode(violation),
                        extractFieldPath(violation)
                ))
                .toList();

        log.warn("Constraint violation: errors={}, requestId={}", fieldErrors.size(), requestId);

        ApiErrorResponse response = new ApiErrorResponse(
                new ApiErrorResponse.ErrorBody(
                        ErrorType.VALIDATION_ERROR,
                        null,
                        null,
                        fieldErrors,
                        null,
                        isDev ? ex.getMessage() : null,
                        requestId
                ),
                400
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException ex) {
        String requestId = getRequestId();

        log.warn("API exception: type={}, code={}, field={}, requestId={}",
                ex.getType(), ex.getCode(), ex.getField(), requestId);

        ApiErrorResponse response = new ApiErrorResponse(
                new ApiErrorResponse.ErrorBody(
                        ex.getType(),
                        ex.getCode(),
                        ex.getField(),
                        null,
                        ex.getMeta(),
                        getDebugMessage(ex),
                        requestId
                ),
                ex.getHttpStatus()
        );

        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthorizationDenied(AuthorizationDeniedException ex) {
        String requestId = getRequestId();

        log.warn("Authorization denied: requestId={}", requestId);

        ApiErrorResponse response = ApiErrorResponse.authorization(ErrorCode.INSUFFICIENT_PRIVILEGES, requestId);

        return ResponseEntity.status(response.statusCode()).body(response);
    }

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

    private String getDebugMessage(Exception ex) {
        return isDev ? ex.getMessage() : null;
    }

    private ErrorCode.Validation mapValidationCode(String code, String field, Object rejectedValue) {
        if (code == null) {
            return ErrorCode.Validation.INVALID_FORMAT;
        }

        return switch (code) {
            case "NotBlank", "NotEmpty", "NotNull" -> ErrorCode.Validation.REQUIRED;
            case "Size", "Length" -> mapSizeValidation(rejectedValue);
            case "Min" -> ErrorCode.Validation.OUT_OF_RANGE;
            case "Max" -> ErrorCode.Validation.OUT_OF_RANGE;
            case "Range", "Positive", "PositiveOrZero", "Negative", "NegativeOrZero" ->
                    ErrorCode.Validation.OUT_OF_RANGE;
            case "Email" -> ErrorCode.Validation.INVALID_EMAIL;
            case "Pattern", "Digits" -> mapPatternValidation(field);
            case "URL" -> ErrorCode.Validation.INVALID_URL;
            case "Past", "PastOrPresent", "Future", "FutureOrPresent" -> ErrorCode.Validation.INVALID_DATE;
            case "UUID" -> ErrorCode.Validation.INVALID_UUID;
            case "PasswordMatches" -> ErrorCode.Validation.PASSWORD_MISMATCH;
            default -> ErrorCode.Validation.INVALID_FORMAT;
        };
    }

    private String normalizeFieldPath(String field) {
        if (field == null) {
            return null;
        }
        Matcher matcher = ARRAY_INDEX_PATTERN.matcher(field);
        return matcher.replaceAll(".$1");
    }

    private ErrorCode.Validation mapSizeValidation(Object rejectedValue) {
        if (rejectedValue == null) {
            return ErrorCode.Validation.REQUIRED;
        }

        if (rejectedValue instanceof String str) {
            if (str.isEmpty() || str.length() < 3) {
                return ErrorCode.Validation.TOO_SHORT;
            }

            if (str.length() > 100) {
                return ErrorCode.Validation.TOO_LONG;
            }
        }

        return ErrorCode.Validation.TOO_SHORT;
    }

    private String extractFieldPath(ConstraintViolation<?> violation) {
        String path = violation.getPropertyPath().toString();
        // Remove prefixos de método se existirem (ex: "create.dto.email" -> "email")
        if (path.contains(".")) {
            // Se o path tem mais de 2 partes, provavelmente tem método no início
            String[] parts = path.split("\\.");
            if (parts.length > 2) {
                // Retorna a partir do segundo elemento (pula método e nome do parâmetro)
                return normalizeFieldPath(String.join(".", java.util.Arrays.copyOfRange(parts, 2, parts.length)));
            }
        }
        return normalizeFieldPath(path);
    }

    private ErrorCode.Validation mapConstraintCode(ConstraintViolation<?> violation) {
        String annotationType = violation.getConstraintDescriptor()
                .getAnnotation()
                .annotationType()
                .getSimpleName();

        String field = extractFieldPath(violation);
        Object rejectedValue = violation.getInvalidValue();

        return mapValidationCode(annotationType, field, rejectedValue);
    }

    private ErrorCode.Validation mapPatternValidation(String field) {
        if (field != null && (field.contains("password") || field.contains("Password"))) {
            return ErrorCode.Validation.PASSWORD_TOO_WEAK;
        }

        return ErrorCode.Validation.INVALID_FORMAT;
    }
}
