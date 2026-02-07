package com.smlaurindo.risanailstudio.adapter.outbound.persitence.repository;

import com.smlaurindo.risanailstudio.adapter.outbound.persitence.entity.CustomerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerJpaEntity, String> {}