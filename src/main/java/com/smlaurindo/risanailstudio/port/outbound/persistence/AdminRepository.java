package com.smlaurindo.risanailstudio.port.outbound.persistence;

import com.smlaurindo.risanailstudio.application.domain.Admin;

import java.util.Optional;

public interface AdminRepository {
    Admin save(Admin admin);
    Optional<Admin> findByCredentialsId(String credentialsId);
}
