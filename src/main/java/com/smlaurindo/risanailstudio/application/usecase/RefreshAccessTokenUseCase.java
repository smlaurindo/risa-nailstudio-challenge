package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.port.outbound.persistence.CredentialsRepository;
import com.smlaurindo.risanailstudio.port.outbound.persistence.RefreshTokenRepository;
import com.smlaurindo.risanailstudio.port.outbound.security.TokenGenerator;
import com.smlaurindo.risanailstudio.shared.exception.AuthenticationException;
import com.smlaurindo.risanailstudio.shared.exception.ErrorCode;

import java.time.Instant;

public class RefreshAccessTokenUseCase implements RefreshAccessToken {

    private final RefreshTokenRepository refreshTokenRepository;
    private final CredentialsRepository credentialsRepository;
    private final TokenGenerator tokenGenerator;

    public RefreshAccessTokenUseCase(
            RefreshTokenRepository refreshTokenRepository,
            CredentialsRepository credentialsRepository,
            TokenGenerator tokenGenerator
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.credentialsRepository = credentialsRepository;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public RefreshAccessTokenOutput refreshAccessToken(RefreshAccessTokenInput input) {
        var refreshToken = refreshTokenRepository.findByToken(input.refreshToken())
                .orElseThrow(() -> new AuthenticationException(ErrorCode.INVALID_TOKEN));

        if (refreshToken.isRevoked()) {
            throw new AuthenticationException(ErrorCode.TOKEN_REVOKED);
        }

        if (refreshToken.isExpired(Instant.now())) {
            throw new AuthenticationException(ErrorCode.EXPIRED_TOKEN);
        }

        var credentials = credentialsRepository.findById(refreshToken.getSubject())
                .orElseThrow(() -> new AuthenticationException(ErrorCode.INVALID_CREDENTIALS));

        var accessToken = tokenGenerator.generateAccessToken(credentials.getId(), credentials.getRole());

        return new RefreshAccessTokenOutput(accessToken);
    }
}
