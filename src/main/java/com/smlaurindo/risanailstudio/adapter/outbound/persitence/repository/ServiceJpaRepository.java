package com.smlaurindo.risanailstudio.adapter.outbound.persitence.repository;

import com.smlaurindo.risanailstudio.adapter.outbound.persitence.entity.ServiceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceJpaRepository extends JpaRepository<ServiceJpaEntity, String> {}
