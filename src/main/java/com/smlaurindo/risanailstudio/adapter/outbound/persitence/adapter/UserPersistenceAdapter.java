package com.smlaurindo.risanailstudio.adapter.outbound.persitence.adapter;

import com.smlaurindo.risanailstudio.adapter.outbound.persitence.entity.UserJpaEntity;
import com.smlaurindo.risanailstudio.adapter.outbound.persitence.repository.UserJpaRepository;
import com.smlaurindo.risanailstudio.application.domain.Credentials;
import com.smlaurindo.risanailstudio.port.outbound.persistence.CredentialsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements CredentialsRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Credentials save(Credentials credentials) {
        log.debug("Saving user: {}", credentials.getId());

        UserJpaEntity entity = UserJpaEntity.builder()
                .id(credentials.getId())
                .email(credentials.getEmail())
                .passwordHash(credentials.getPasswordHash())
                .role(credentials.getRole())
                .build();

        UserJpaEntity saved = userJpaRepository.save(entity);

        return new Credentials(
                saved.getId(),
                saved.getEmail(),
                saved.getPasswordHash(),
                saved.getRole()
        );
    }

    @Override
    public boolean existsByEmail(String email) {
        log.debug("Checking if user exists by email: {}", email);

        return userJpaRepository.existsByEmail(email);
    }
}
