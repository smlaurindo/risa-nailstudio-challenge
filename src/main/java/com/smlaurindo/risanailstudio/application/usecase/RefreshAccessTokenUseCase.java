package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.port.outbound.persistence.CredentialsRepositoryPort;
import com.smlaurindo.risanailstudio.port.outbound.persistence.RefreshTokenRepositoryPort;
import com.smlaurindo.risanailstudio.port.outbound.security.TokenGeneratorPort;
import com.smlaurindo.risanailstudio.application.exception.AuthenticationException;
import com.smlaurindo.risanailstudio.application.exception.ErrorCode;

import java.time.Instant;

public class RefreshAccessTokenUseCase implements RefreshAccessToken {

    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;
    private final CredentialsRepositoryPort credentialsRepositoryPort;
    private final TokenGeneratorPort tokenGeneratorPort;

    public RefreshAccessTokenUseCase(
            RefreshTokenRepositoryPort refreshTokenRepositoryPort,
            CredentialsRepositoryPort credentialsRepositoryPort,
            TokenGeneratorPort tokenGeneratorPort
    ) {
        this.refreshTokenRepositoryPort = refreshTokenRepositoryPort;
        this.credentialsRepositoryPort = credentialsRepositoryPort;
        this.tokenGeneratorPort = tokenGeneratorPort;
    }

    @Override
    public RefreshAccessTokenOutput refreshAccessToken(RefreshAccessTokenInput input) {
        var refreshToken = refreshTokenRepositoryPort.findByToken(input.refreshToken())
                .orElseThrow(() -> new AuthenticationException(ErrorCode.INVALID_TOKEN));

        if (refreshToken.isRevoked()) {
            throw new AuthenticationException(ErrorCode.TOKEN_REVOKED);
        }

        if (refreshToken.isExpired(Instant.now())) {
            throw new AuthenticationException(ErrorCode.EXPIRED_TOKEN);
        }

        var credentials = credentialsRepositoryPort.findById(refreshToken.getSubject())
                .orElseThrow(() -> new AuthenticationException(ErrorCode.INVALID_CREDENTIALS));

        var accessToken = tokenGeneratorPort.generateAccessToken(credentials.getId(), credentials.getRole());

        return new RefreshAccessTokenOutput(accessToken);
    }
}
