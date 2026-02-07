package com.smlaurindo.risanailstudio.adapter.outbound.persitence.repository;


import com.smlaurindo.risanailstudio.adapter.outbound.persitence.entity.RefreshTokenJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenJpaEntity, String> {
     //RefreshToken findByToken(String token);
}
