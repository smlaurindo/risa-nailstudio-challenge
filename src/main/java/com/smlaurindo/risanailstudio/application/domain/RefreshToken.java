package com.smlaurindo.risanailstudio.application.domain;

import java.time.Instant;

import static java.util.UUID.randomUUID;

public class RefreshToken {
    private final String id;
    private final String token;
    private final String subject;
    private final Instant issuedAt;
    private final Instant expiresAt;
    private boolean revoked;

    public RefreshToken(
            String subject,
            Instant issuedAt,
            Instant expiresAt
    ) {
        this.id = randomUUID().toString();
        this.token = randomUUID().toString();
        this.subject = subject;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.revoked = false;
    }

    public RefreshToken(String id, String token, String subject, Instant issuedAt, Instant expiresAt, boolean revoked) {
        this.id = id;
        this.token = token;
        this.subject = subject;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.revoked = revoked;
    }


    public boolean isExpired(Instant now) {
        return now.isAfter(expiresAt);
    }

    public void revoke() {
        this.revoked = true;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getSubject() {
        return subject;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public boolean isRevoked() {
        return revoked;
    }
}

