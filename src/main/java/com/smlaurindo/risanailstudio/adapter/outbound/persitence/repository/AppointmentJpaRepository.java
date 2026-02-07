package com.smlaurindo.risanailstudio.adapter.outbound.persitence.repository;

import com.smlaurindo.risanailstudio.adapter.outbound.persitence.entity.AppointmentJpaEntity;
import com.smlaurindo.risanailstudio.adapter.outbound.persitence.repository.projection.AppointmentToListProjection;
import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

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
            @Param("startsAt") Instant startsAt,
            @Param("endsAt") Instant endsAt
    );

    @Query("""
        SELECT
            a.id as id,
            c.id as customerId,
            c.name as customerName,
            c.photo as customerPhoto,
            s.id as serviceId,
            s.name as serviceName,
            a.status as status,
            a.startsAt as startsAt,
            a.endsAt as endsAt
        FROM AppointmentJpaEntity a
        JOIN a.customer c
        JOIN a.service s
        WHERE a.startsAt > :startDate
          AND a.endsAt < :endDate
          AND (:status IS NULL OR a.status = :status)
          AND (
              COALESCE(:searchQuery, '') = ''
              OR c.name ILIKE CONCAT('%', COALESCE(:searchQuery, ''), '%')
              OR s.name ILIKE CONCAT('%', COALESCE(:searchQuery, ''), '%')
          )
        ORDER BY a.startsAt ASC
    """)
    List<AppointmentToListProjection> findAppointments(
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            @Param("status") AppointmentStatus status,
            @Param("searchQuery") String searchQuery
    );
}
