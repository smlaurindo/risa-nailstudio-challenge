package com.smlaurindo.risanailstudio.port.outbound.persistence;

import com.smlaurindo.risanailstudio.application.domain.RefreshToken;

public interface RefreshTokenRepository {
    RefreshToken save(RefreshToken refreshToken);
}
