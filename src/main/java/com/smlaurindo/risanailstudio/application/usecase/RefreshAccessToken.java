package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.port.outbound.security.TokenGenerator;

public interface RefreshAccessToken {
    record RefreshAccessTokenInput(
            String refreshToken
    ) {}

    record RefreshAccessTokenOutput(
            TokenGenerator.AccessToken accessToken
    ) {}

    RefreshAccessTokenOutput refreshAccessToken(RefreshAccessTokenInput input);
}
