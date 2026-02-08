package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.port.outbound.persistence.CredentialsRepositoryPort;
import com.smlaurindo.risanailstudio.port.outbound.security.PasswordHasherPort;
import com.smlaurindo.risanailstudio.port.outbound.security.TokenGeneratorPort;
import com.smlaurindo.risanailstudio.application.exception.AuthenticationException;
import com.smlaurindo.risanailstudio.application.exception.ErrorCode;

public class SignInUseCase implements SignIn {

    private final TokenGeneratorPort tokenGeneratorPort;
    private final CredentialsRepositoryPort credentialsRepositoryPort;
    private final PasswordHasherPort passwordHasherPort;

    public SignInUseCase(TokenGeneratorPort tokenGeneratorPort, CredentialsRepositoryPort credentialsRepositoryPort, PasswordHasherPort passwordHasherPort) {
        this.tokenGeneratorPort = tokenGeneratorPort;
        this.credentialsRepositoryPort = credentialsRepositoryPort;
        this.passwordHasherPort = passwordHasherPort;
    }

    @Override
    public SignInOutput signIn(SignInInput input) {
        var credentials = credentialsRepositoryPort.findByEmail(input.email())
                .orElseThrow(() -> new AuthenticationException(ErrorCode.INVALID_CREDENTIALS));

        boolean passwordMatches = passwordHasherPort.matches(
                input.password(),
                credentials.getPasswordHash()
        );

        if (!passwordMatches) {
            throw new AuthenticationException(ErrorCode.INVALID_CREDENTIALS);
        }

        var accessToken = tokenGeneratorPort.generateAccessToken(credentials.getId(), credentials.getRole());
        var refreshToken = tokenGeneratorPort.generateRefreshToken(credentials.getId());

        return new SignInOutput(accessToken, refreshToken);
    }
}
