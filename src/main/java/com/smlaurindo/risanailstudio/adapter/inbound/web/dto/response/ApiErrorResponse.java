package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smlaurindo.risanailstudio.application.exception.ErrorCode;
import com.smlaurindo.risanailstudio.application.exception.ErrorType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(description = "Standard API error response")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(
        @Schema(description = "Error body with details")
        ErrorBody error,
        
        @Schema(description = "HTTP status code", example = "400")
        int statusCode
) {
    /**
     * Error body with all details.
     *
     * @param type      Error category (validation_error, authentication_error, etc)
     * @param code      Semantic error code (INVALID_CREDENTIALS, USER_ALREADY_EXISTS, etc)
     * @param field     Field related to the error (for single field errors like conflict)
     * @param errors    List of validation errors (for validation_error with multiple fields)
     * @param meta      Additional metadata (retryAfterSeconds, required, current, etc)
     * @param message   Debug message (only in non-production environments)
     * @param requestId Unique request ID for correlation in logs
     */
    @Schema(description = "Error details")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ErrorBody(
            @Schema(description = "Error type", example = "validation_error")
            ErrorType type,
            
            @Schema(description = "Specific error code", example = "INVALID_CREDENTIALS")
            ErrorCode code,
            
            @Schema(description = "Field related to the error")
            String field,
            
            @Schema(description = "List of validation errors (when multiple fields have errors)")
            List<FieldError> errors,
            
            @Schema(description = "Additional metadata about the error")
            Map<String, Object> meta,
            
            @Schema(description = "Debug message (development only)")
            String message,
            
            @Schema(description = "Unique request ID", example = "req_abc123")
            String requestId
    ) {
        public ErrorBody(ErrorType type, ErrorCode code, String requestId) {
            this(type, code, null, null, null, null, requestId);
        }
    }

    @Schema(description = "Field validation error")
    public record FieldError(
            @Schema(description = "Validation error code", example = "REQUIRED")
            ErrorCode.Validation code,
            
            @Schema(description = "Field name with error", example = "email")
            String field
    ) {}

    public static ApiErrorResponse authentication(ErrorCode code, String requestId) {
        return new ApiErrorResponse(
                new ErrorBody(ErrorType.AUTHENTICATION_ERROR, code, requestId),
                401
        );
    }

    public static ApiErrorResponse authorization(ErrorCode code, String requestId) {
        return new ApiErrorResponse(
                new ErrorBody(ErrorType.AUTHORIZATION_ERROR, code, requestId),
                403
        );
    }

    public static ApiErrorResponse internal(String message, String requestId) {
        return new ApiErrorResponse(
                new ErrorBody(
                        ErrorType.INTERNAL_ERROR,
                        ErrorCode.INTERNAL_ERROR,
                        null,
                        null,
                        null,
                        message,
                        requestId
                ),
                500
        );
    }
}
