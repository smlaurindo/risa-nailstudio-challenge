package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.port.outbound.security.TokenGeneratorPort;

public interface SignIn {
    record SignInInput(
            String email,
            String password
    ) {}

    record SignInOutput(
            TokenGeneratorPort.AccessToken accessToken,
            TokenGeneratorPort.RefreshToken refreshToken
    ) {}

    SignInOutput signIn(SignInInput input);
}
