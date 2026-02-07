package com.smlaurindo.risanailstudio.shared.config.security;

import com.smlaurindo.risanailstudio.shared.dto.ApiErrorResponse;
import com.smlaurindo.risanailstudio.application.exception.ErrorCode;
import com.smlaurindo.risanailstudio.shared.filter.RequestIdFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        String requestId = MDC.get(RequestIdFilter.REQUEST_ID_MDC_KEY);
        if (requestId == null) {
            requestId = "unknown";
        }

        log.warn("Authentication failed in security filter: type={}, message={}, requestId={}, path={}",
                authException.getClass().getSimpleName(),
                authException.getMessage(),
                requestId,
                request.getRequestURI());

        ApiErrorResponse errorResponse = ApiErrorResponse.authentication(
                ErrorCode.INVALID_CREDENTIALS,
                requestId
        );

        response.setStatus(errorResponse.statusCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
