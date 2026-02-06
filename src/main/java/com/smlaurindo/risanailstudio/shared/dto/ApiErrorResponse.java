package com.smlaurindo.risanailstudio.shared.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smlaurindo.risanailstudio.shared.exception.ErrorCode;
import com.smlaurindo.risanailstudio.shared.exception.ErrorType;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(
        ErrorBody error,
        int statusCode
) {
    /**
     * Corpo do erro com todos os detalhes.
     *
     * @param type      Categoria do erro (validation_error, authentication_error, etc)
     * @param code      Código semântico do erro (INVALID_CREDENTIALS, USER_ALREADY_EXISTS, etc)
     * @param field     Campo relacionado ao erro (para erros de campo único como conflict)
     * @param errors    Lista de erros de validação (para validation_error com múltiplos campos)
     * @param meta      Metadados adicionais (retryAfterSeconds, required, current, etc)
     * @param message   Mensagem de debug (apenas em ambientes não-produção)
     * @param requestId ID único da requisição para correlação em logs
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ErrorBody(
            ErrorType type,
            ErrorCode code,
            String field,
            List<FieldError> errors,
            Map<String, Object> meta,
            String message,
            String requestId
    ) {}

    public record FieldError(
            ErrorCode.Validation code,
            String field
    ) {}

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
