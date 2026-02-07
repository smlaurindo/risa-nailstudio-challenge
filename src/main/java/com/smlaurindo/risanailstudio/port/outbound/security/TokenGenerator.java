package com.smlaurindo.risanailstudio.port.outbound.security;

import com.smlaurindo.risanailstudio.application.domain.Role;

import java.time.Instant;
import java.util.List;

public interface TokenGenerator {
    record AccessToken(
        String token,
        String subject,
        Instant expiresAt,
        Instant issuedAt,
        List<String> roles
    ) {}

    record RefreshToken(
            String token,
            String subject,
            Instant issuedAt,
            Instant expiresAt
    ) {}

    AccessToken generateAccessToken(String subject, Role role);

    RefreshToken generateRefreshToken(String subject);
}
