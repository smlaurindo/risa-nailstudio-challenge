package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.port.outbound.persistence.RefreshTokenRepository;
import com.smlaurindo.risanailstudio.shared.exception.AuthenticationException;
import com.smlaurindo.risanailstudio.shared.exception.ErrorCode;

public class SignOutUseCase implements SignOut {

    private final RefreshTokenRepository refreshTokenRepository;

    public SignOutUseCase(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void signOut(SignOutInput input) {
        var refreshToken = refreshTokenRepository.findByToken(input.refreshToken())
                .orElseThrow(() -> new AuthenticationException(ErrorCode.INVALID_TOKEN));

        if (refreshToken.isRevoked()) {
            throw new AuthenticationException(ErrorCode.TOKEN_REVOKED);
        }

        refreshToken.revoke();

        refreshTokenRepository.save(refreshToken);
    }
}
