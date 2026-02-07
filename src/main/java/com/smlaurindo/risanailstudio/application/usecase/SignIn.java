package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.port.outbound.security.TokenGenerator;

public interface SignIn {
    record SignInInput(
            String email,
            String password
    ) {}

    record SignInOutput(
            TokenGenerator.AccessToken accessToken,
            TokenGenerator.RefreshToken refreshToken
    ) {}

    SignInOutput signIn(SignInInput input);
}
