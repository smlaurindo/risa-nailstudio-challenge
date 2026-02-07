package com.smlaurindo.risanailstudio.adapter.outbound.persitence.repository;

import com.smlaurindo.risanailstudio.adapter.outbound.persitence.entity.CustomerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerJpaEntity, String> {
    Optional<CustomerJpaEntity> findByUserId(String userId);
}