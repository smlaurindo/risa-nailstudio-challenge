package com.smlaurindo.risanailstudio.adapter.outbound.persitence.adapter;

import com.smlaurindo.risanailstudio.adapter.outbound.persitence.entity.RefreshTokenJpaEntity;
import com.smlaurindo.risanailstudio.adapter.outbound.persitence.entity.UserJpaEntity;
import com.smlaurindo.risanailstudio.adapter.outbound.persitence.repository.RefreshTokenJpaRepository;
import com.smlaurindo.risanailstudio.application.domain.RefreshToken;
import com.smlaurindo.risanailstudio.port.outbound.persistence.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenPersistenceAdapter implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        log.debug("Saving refresh token with id: {}", refreshToken.getId());

        RefreshTokenJpaEntity entity = RefreshTokenJpaEntity.builder()
                .id(refreshToken.getId())
                .token(refreshToken.getToken())
                .subject(new UserJpaEntity(refreshToken.getSubject()))
                .issuedAt(refreshToken.getIssuedAt())
                .expiresAt(refreshToken.getExpiresAt())
                .revoked(refreshToken.isRevoked())
                .build();

        RefreshTokenJpaEntity saved = refreshTokenJpaRepository.save(entity);

        return new RefreshToken(
                saved.getId(),
                saved.getToken(),
                saved.getSubject().getId(),
                saved.getIssuedAt(),
                saved.getExpiresAt(),
                saved.isRevoked()
        );
    }

    @Override
    public java.util.Optional<RefreshToken> findByToken(String token) {
        log.debug("Finding refresh token by token");
        
        return refreshTokenJpaRepository.findByToken(token)
                .map(entity -> new RefreshToken(
                        entity.getId(),
                        entity.getToken(),
                        entity.getSubject().getId(),
                        entity.getIssuedAt(),
                        entity.getExpiresAt(),
                        entity.isRevoked()
                ));
    }
}
