package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.port.outbound.persistence.RefreshTokenRepositoryPort;
import com.smlaurindo.risanailstudio.application.exception.AuthenticationException;
import com.smlaurindo.risanailstudio.application.exception.ErrorCode;

public class SignOutUseCase implements SignOut {

    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;

    public SignOutUseCase(RefreshTokenRepositoryPort refreshTokenRepositoryPort) {
        this.refreshTokenRepositoryPort = refreshTokenRepositoryPort;
    }

    @Override
    public void signOut(SignOutInput input) {
        var refreshToken = refreshTokenRepositoryPort.findByToken(input.refreshToken())
                .orElseThrow(() -> new AuthenticationException(ErrorCode.INVALID_TOKEN));

        if (refreshToken.isRevoked()) {
            throw new AuthenticationException(ErrorCode.TOKEN_REVOKED);
        }

        refreshToken.revoke();

        refreshTokenRepositoryPort.save(refreshToken);
    }
}
