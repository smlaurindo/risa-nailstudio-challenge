package com.smlaurindo.risanailstudio.port.outbound.persistence;

import com.smlaurindo.risanailstudio.application.domain.Credentials;

public interface CredentialsRepository {
    boolean existsByEmail(String email);
    Credentials save(Credentials credentials);
}
