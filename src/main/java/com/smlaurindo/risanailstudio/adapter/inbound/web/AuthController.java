package com.smlaurindo.risanailstudio.adapter.inbound.web;

import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request.SignInRequest;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request.SignUpRequest;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.ApiErrorResponse;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.RefreshAccessTokenResponse;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.SignInResponse;
import com.smlaurindo.risanailstudio.application.usecase.RefreshAccessToken;
import com.smlaurindo.risanailstudio.application.usecase.SignIn;
import com.smlaurindo.risanailstudio.application.usecase.SignOut;
import com.smlaurindo.risanailstudio.application.usecase.SignUp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

import static com.smlaurindo.risanailstudio.shared.constant.AuthConstants.ACCESS_TOKEN_COOKIE_NAME;
import static com.smlaurindo.risanailstudio.shared.constant.AuthConstants.REFRESH_TOKEN_COOKIE_NAME;

@Tag(name = "Authentication", description = "Endpoints for user authentication and session management")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final SignUp signUp;
    private final SignIn signIn;
    private final SignOut signOut;
    private final RefreshAccessToken refreshAccessToken;

    @Value("${app.env.is-dev}")
    private boolean isDev;

    @Value("${app.cookie.domain}")
    private String cookieDomain;

    @Operation(
            summary = "Register new user",
            description = "Creates a new customer account in the system. Email must be unique and password must meet security requirements."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User successfully registered"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "error": {
                                        "type": "validation_error",
                                        "errors": [
                                          {
                                            "code": "INVALID_EMAIL",
                                            "field": "email"
                                          },
                                          {
                                            "code": "PASSWORD_TOO_WEAK",
                                            "field": "password"
                                          }
                                        ],
                                        "requestId": "abc123"
                                      },
                                      "statusCode": 400
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email already registered",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "error": {
                                        "type": "conflict",
                                        "code": "USER_ALREADY_EXISTS",
                                        "field": "email",
                                        "requestId": "abc123"
                                      },
                                      "statusCode": 409
                                    }
                                    """)
                    )
            )
    })
    @SecurityRequirements
    @PostMapping(path = "/sign-up")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest request) {
        log.info("Sign up request received for email: {}", request.email());

        var input = SignUpRequest.toInput(request);

        signUp.signUp(input);

        log.info("Sign up successful for email: {}", request.email());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Login",
            description = "Authenticates a user and returns access tokens. Tokens are sent both in the response body and as HTTP-only cookies."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SignInResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de entrada inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "error": {
                                        "type": "authentication_error",
                                        "code": "INVALID_CREDENTIALS",
                                        "requestId": "abc123"
                                      },
                                      "statusCode": 401
                                    }
                                    """)
                    )
            )
    })
    @SecurityRequirements
    @PostMapping(path = "/sign-in")
    public ResponseEntity<SignInResponse> signIn(
            @Valid @RequestBody SignInRequest request
    ) {
        log.info("Login attempt for email: {}", request.email());

        var output = signIn.signIn(request.toInput());

        var accessTokenCookieBuilder = ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, output.accessToken().token())
                .httpOnly(true)
                .secure(!isDev)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.between(output.accessToken().issuedAt(), output.accessToken().expiresAt()));

        var refreshTokenCookieBuilder = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, output.refreshToken().token())
                .httpOnly(true)
                .secure(!isDev)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.between(output.refreshToken().issuedAt(), output.refreshToken().expiresAt()));

        if (!isDev && !cookieDomain.isEmpty()) {
            accessTokenCookieBuilder.domain(cookieDomain);
        }

        ResponseCookie accessTokenCookie = accessTokenCookieBuilder.build();
        ResponseCookie refreshTokenCookie = refreshTokenCookieBuilder.build();

        log.info("Login successful for email: {}", request.email());

        var response = SignInResponse.from(output);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(response);
    }

    @Operation(
            summary = "Refresh access token",
            description = "Renews the access token using the refresh token. The refresh token must be sent via cookie.",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "accessTokenCookieAuth"), @SecurityRequirement(name = "refreshTokenCookieAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Access token successfully refreshed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RefreshAccessTokenResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid, expired or revoked refresh token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "error": {
                                        "type": "authentication_error",
                                        "code": "INVALID_TOKEN",
                                        "requestId": "req_abc123"
                                      },
                                      "statusCode": 401
                                    }
                                    """)
                    )
            )
    })
    @SecurityRequirements
    @PostMapping(path = "/refresh")
    public ResponseEntity<RefreshAccessTokenResponse> refresh(
            @Parameter(hidden = true) @CookieValue(name = REFRESH_TOKEN_COOKIE_NAME) String refreshToken
    ) {
        log.info("Refresh access token attempt");

        var input = new RefreshAccessToken.RefreshAccessTokenInput(refreshToken);
        var output = refreshAccessToken.refreshAccessToken(input);

        var accessTokenCookieBuilder = ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, output.accessToken().token())
                .httpOnly(true)
                .secure(!isDev)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.between(output.accessToken().issuedAt(), output.accessToken().expiresAt()));

        if (!isDev && !cookieDomain.isEmpty()) {
            accessTokenCookieBuilder.domain(cookieDomain);
        }

        ResponseCookie accessTokenCookie = accessTokenCookieBuilder.build();

        log.info("Access token refreshed successfully");

        var response = RefreshAccessTokenResponse.from(output);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .body(response);
    }

    @Operation(
            summary = "Logout",
            description = "Invalidates the current refresh token and clears authentication cookies.",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "accessTokenCookieAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Logout successful"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid or missing token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                                                {
                                                    "error": {
                                                        "type": "authentication_error",
                                                        "code": "INVALID_CREDENTIALS",
                                                        "requestId": "req_l5d2o16e4yxi"
                                                    },
                                                    "statusCode": 401
                                                }
                                                """)
                    )
            )
    })
    @SecurityRequirements
    @PostMapping(path = "/sign-out")
    public ResponseEntity<Void> signOut(
            @Parameter(hidden = true)
            @CookieValue(name = REFRESH_TOKEN_COOKIE_NAME)
            String refreshToken
    ) {
        log.info("Logout attempt with refresh token");

        var input = new SignOut.SignOutInput(refreshToken);

        signOut.signOut(input);

        var accessTokenCookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(!isDev)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        var refreshTokenCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(!isDev)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        log.info("Logout successful");

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .build();
    }
}
