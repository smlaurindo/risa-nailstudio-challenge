package com.smlaurindo.risanailstudio.adapter.outbound.persitence.repository;

import com.smlaurindo.risanailstudio.adapter.outbound.persitence.entity.AppointmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AppointmentJpaRepository extends JpaRepository<AppointmentJpaEntity, String> {
    @Query("""
        SELECT count(a) > 0
        FROM AppointmentJpaEntity a
        WHERE a.startsAt < :endsAt
        AND a.endsAt > :startsAt
        AND a.status <> 'CANCELLED'
    """)
    boolean existsConflict(
            @Param("startsAt") LocalDateTime startsAt,
            @Param("endsAt") LocalDateTime endsAt
    );
}
