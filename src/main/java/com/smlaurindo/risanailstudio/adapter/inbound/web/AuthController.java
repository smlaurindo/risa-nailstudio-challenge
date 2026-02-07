package com.smlaurindo.risanailstudio.adapter.inbound.web;

import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request.SignInRequest;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request.SignUpRequest;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.RefreshAccessTokenResponse;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.SignInResponse;
import com.smlaurindo.risanailstudio.application.usecase.RefreshAccessToken;
import com.smlaurindo.risanailstudio.application.usecase.SignIn;
import com.smlaurindo.risanailstudio.application.usecase.SignOut;
import com.smlaurindo.risanailstudio.application.usecase.SignUp;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

import static com.smlaurindo.risanailstudio.shared.constant.AuthConstants.ACCESS_TOKEN_COOKIE_NAME;
import static com.smlaurindo.risanailstudio.shared.constant.AuthConstants.REFRESH_TOKEN_COOKIE_NAME;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final SignUp signUp;
    private final SignIn signIn;
    private final SignOut signOut;
    private final RefreshAccessToken refreshAccessToken;

    @Value("${app.env.is-dev}")
    private boolean isDev;

    @Value("${app.cookie.domain}")
    private String cookieDomain;

    @PostMapping(value = "/auth/sign-up", version = "1")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest request) {
        log.info("Sign up request received for email: {}", request.email());

        var input = SignUpRequest.toInput(request);

        signUp.signUp(input);

        log.info("Sign up successful for email: {}", request.email());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/auth/sign-in", version = "1")
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

    @PostMapping(value = "/auth/refresh", version = "1")
    public ResponseEntity<RefreshAccessTokenResponse> refresh(
            @CookieValue(name = REFRESH_TOKEN_COOKIE_NAME) String refreshToken
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

    @PostMapping(value = "/auth/sign-out", version = "1")
    public ResponseEntity<Void> signOut(@CookieValue(name = REFRESH_TOKEN_COOKIE_NAME) String refreshToken) {
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
