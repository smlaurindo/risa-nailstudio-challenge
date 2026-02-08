package com.smlaurindo.risanailstudio.port.outbound.persistence;

import com.smlaurindo.risanailstudio.application.domain.Admin;

import java.util.Optional;

public interface AdminRepositoryPort {
    Admin save(Admin admin);
    Optional<Admin> findByCredentialsId(String credentialsId);
}
