package com.smlaurindo.risanailstudio.adapter.outbound.persitence.adapter;

import com.smlaurindo.risanailstudio.adapter.outbound.persitence.entity.AdminJpaEntity;
import com.smlaurindo.risanailstudio.adapter.outbound.persitence.entity.UserJpaEntity;
import com.smlaurindo.risanailstudio.adapter.outbound.persitence.repository.AdminJpaRepository;
import com.smlaurindo.risanailstudio.application.domain.Admin;
import com.smlaurindo.risanailstudio.port.outbound.persistence.AdminRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminRepositoryAdapter implements AdminRepositoryPort {

    private final AdminJpaRepository adminJpaRepository;

    @Override
    public Admin save(Admin admin) {
        log.debug("Saving admin: {}", admin.getId());

        AdminJpaEntity entity = AdminJpaEntity.builder()
                .id(admin.getId())
                .user(new UserJpaEntity(admin.getCredentialsId()))
                .name(admin.getName())
                .build();

        AdminJpaEntity saved = adminJpaRepository.save(entity);

        return new Admin(
                saved.getId(),
                saved.getUser().getId(),
                saved.getName()
        );
    }

    @Override
    public Optional<Admin> findByCredentialsId(String credentialsId) {
        log.debug("Finding admin by credentialsId: {}", credentialsId);

        return adminJpaRepository.findByUserId(credentialsId)
                .map(entity -> new Admin(
                        entity.getId(),
                        entity.getUser().getId(),
                        entity.getName()
                ));
    }
}
