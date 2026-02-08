package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response;

import com.smlaurindo.risanailstudio.application.usecase.RefreshAccessToken;
import com.smlaurindo.risanailstudio.port.outbound.security.TokenGeneratorPort;

public record RefreshAccessTokenResponse(
        TokenGeneratorPort.AccessToken accessToken
) {
    public static RefreshAccessTokenResponse from(RefreshAccessToken.RefreshAccessTokenOutput output) {
        return new RefreshAccessTokenResponse(output.accessToken());
    }
}
