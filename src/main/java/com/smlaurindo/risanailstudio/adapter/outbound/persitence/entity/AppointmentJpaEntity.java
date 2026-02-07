package com.smlaurindo.risanailstudio.adapter.outbound.persitence.entity;

import com.smlaurindo.risanailstudio.application.domain.Appointment;
import com.smlaurindo.risanailstudio.application.domain.AppointmentSlot;
import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;
import com.smlaurindo.risanailstudio.application.domain.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentJpaEntity {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CustomerJpaEntity customer;

    @ManyToOne(fetch = FetchType.LAZY)
    private ServiceJpaEntity service;

    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;

    @Column(name = "ends_at", nullable = false)
    private LocalDateTime endsAt;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Column(name = "accepted_at", nullable = false)
    private Instant acceptedAt;

    @Column(name = "cancelled_at", nullable = false)
    private Instant cancelledAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public static AppointmentJpaEntity fromDomain(Appointment appointment) {
        return new AppointmentJpaEntity(
                appointment.getId(),
                new CustomerJpaEntity(appointment.getCustomerId()),
                new ServiceJpaEntity(appointment.getServiceId()),
                appointment.getSlot().startsAt(),
                appointment.getSlot().endsAt(),
                appointment.getStatus(),
                appointment.getAcceptedAt(),
                appointment.getCancelledAt(),
                appointment.getCreatedAt()
        );
    }

    public Appointment toDomain() {
        return new Appointment(
                id,
                customer.getId(),
                service.getId(),
                new AppointmentSlot(startsAt, endsAt),
                status,
                acceptedAt,
                cancelledAt,
                createdAt
        );
    }
}
