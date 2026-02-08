package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response;

import com.smlaurindo.risanailstudio.application.usecase.SignIn;
import com.smlaurindo.risanailstudio.port.outbound.security.TokenGeneratorPort;

public record SignInResponse(
        TokenGeneratorPort.AccessToken accessToken,
        TokenGeneratorPort.RefreshToken refreshToken
) {
    public static SignInResponse from(SignIn.SignInOutput output) {
        return new SignInResponse(output.accessToken(), output.refreshToken());
    }
}
