package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.port.outbound.security.TokenGeneratorPort;

public interface RefreshAccessToken {
    record RefreshAccessTokenInput(
            String refreshToken
    ) {}

    record RefreshAccessTokenOutput(
            TokenGeneratorPort.AccessToken accessToken
    ) {}

    RefreshAccessTokenOutput refreshAccessToken(RefreshAccessTokenInput input);
}
