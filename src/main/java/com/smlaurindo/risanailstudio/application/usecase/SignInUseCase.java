package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.port.outbound.persistence.CredentialsRepository;
import com.smlaurindo.risanailstudio.port.outbound.security.PasswordHasher;
import com.smlaurindo.risanailstudio.port.outbound.security.TokenGenerator;
import com.smlaurindo.risanailstudio.application.exception.AuthenticationException;
import com.smlaurindo.risanailstudio.application.exception.ErrorCode;

public class SignInUseCase implements SignIn {

    private final TokenGenerator tokenGenerator;
    private final CredentialsRepository credentialsRepository;
    private final PasswordHasher passwordHasher;

    public SignInUseCase(TokenGenerator tokenGenerator, CredentialsRepository credentialsRepository, PasswordHasher passwordHasher) {
        this.tokenGenerator = tokenGenerator;
        this.credentialsRepository = credentialsRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public SignInOutput signIn(SignInInput input) {
        var credentials = credentialsRepository.findByEmail(input.email())
                .orElseThrow(() -> new AuthenticationException(ErrorCode.INVALID_CREDENTIALS));

        boolean passwordMatches = passwordHasher.matches(
                input.password(),
                credentials.getPasswordHash()
        );

        if (!passwordMatches) {
            throw new AuthenticationException(ErrorCode.INVALID_CREDENTIALS);
        }

        var accessToken = tokenGenerator.generateAccessToken(credentials.getId(), credentials.getRole());
        var refreshToken = tokenGenerator.generateRefreshToken(credentials.getId());

        return new SignInOutput(accessToken, refreshToken);
    }
}
