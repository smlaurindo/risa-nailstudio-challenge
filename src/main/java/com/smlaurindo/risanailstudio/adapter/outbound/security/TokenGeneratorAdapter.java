package com.smlaurindo.risanailstudio.adapter.outbound.security;

import com.smlaurindo.risanailstudio.application.domain.Role;
import com.smlaurindo.risanailstudio.port.outbound.persistence.RefreshTokenRepository;
import com.smlaurindo.risanailstudio.port.outbound.security.TokenGenerator;
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
public class TokenGeneratorAdapter implements TokenGenerator {

    private final JwtEncoder jwtEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

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
    public TokenGenerator.RefreshToken generateRefreshToken(String subject) {
        var now = Instant.now();
        var expiresAt = now.plusMillis(refreshTokenExpiration);

        var refreshToken = new com.smlaurindo.risanailstudio.application.domain.RefreshToken(
                subject,
                now,
                expiresAt
        );

        refreshTokenRepository.save(refreshToken);

        return new TokenGenerator.RefreshToken(
                refreshToken.getToken(),
                refreshToken.getSubject(),
                refreshToken.getIssuedAt(),
                refreshToken.getExpiresAt()
        );
    }
}
