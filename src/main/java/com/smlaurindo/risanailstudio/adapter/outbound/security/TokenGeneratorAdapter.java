package com.smlaurindo.risanailstudio.adapter.outbound.security;

import com.smlaurindo.risanailstudio.application.domain.Role;
import com.smlaurindo.risanailstudio.port.outbound.persistence.RefreshTokenRepositoryPort;
import com.smlaurindo.risanailstudio.port.outbound.security.TokenGeneratorPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

import static java.time.Instant.now;

@Component
@RequiredArgsConstructor
public class TokenGeneratorAdapter implements TokenGeneratorPort {

    private final JwtEncoder jwtEncoder;
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;

    @Value("${app.jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${app.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    @Override
    public AccessToken generateAccessToken(String subject, Role role) {
        var now = now();
        var expiresAt = now.plusMillis(accessTokenExpiration);

        var claims = JwtClaimsSet.builder()
                .subject(subject)
                .issuedAt(now())
                .claim("roles", List.of(role.name()))
                .expiresAt(expiresAt)
                .build();

        var accessToken = jwtEncoder.encode(JwtEncoderParameters.from(claims));

        return new AccessToken(
                accessToken.getTokenValue(),
                accessToken.getSubject(),
                accessToken.getExpiresAt(),
                accessToken.getIssuedAt(),
                List.of(role.name())
        );
    }

    @Override
    public TokenGeneratorPort.RefreshToken generateRefreshToken(String subject) {
        var now = Instant.now();
        var expiresAt = now.plusMillis(refreshTokenExpiration);

        var refreshToken = new com.smlaurindo.risanailstudio.application.domain.RefreshToken(
                subject,
                now,
                expiresAt
        );

        refreshTokenRepositoryPort.save(refreshToken);

        return new TokenGeneratorPort.RefreshToken(
                refreshToken.getToken(),
                refreshToken.getSubject(),
                refreshToken.getIssuedAt(),
                refreshToken.getExpiresAt()
        );
    }
}
