package com.smlaurindo.risanailstudio.adapter.outbound.persitence.repository;


import com.smlaurindo.risanailstudio.adapter.outbound.persitence.entity.RefreshTokenJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenJpaEntity, String> {
     Optional<RefreshTokenJpaEntity> findByToken(String token);
}
