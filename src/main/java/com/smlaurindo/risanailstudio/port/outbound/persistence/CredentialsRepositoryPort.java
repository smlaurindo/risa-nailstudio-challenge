package com.smlaurindo.risanailstudio.port.outbound.persistence;

import com.smlaurindo.risanailstudio.application.domain.Credentials;

import java.util.Optional;

public interface CredentialsRepositoryPort {
    boolean existsByEmail(String email);
    Credentials save(Credentials credentials);
    Optional<Credentials> findByEmail(String email);
    Optional<Credentials> findById(String id);
}
