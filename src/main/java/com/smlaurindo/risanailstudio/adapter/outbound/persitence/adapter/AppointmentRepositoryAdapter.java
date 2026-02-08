package com.smlaurindo.risanailstudio.adapter.outbound.persitence.adapter;

import com.smlaurindo.risanailstudio.adapter.outbound.persitence.entity.AppointmentJpaEntity;
import com.smlaurindo.risanailstudio.adapter.outbound.persitence.repository.AppointmentJpaRepository;
import com.smlaurindo.risanailstudio.application.domain.Appointment;
import com.smlaurindo.risanailstudio.application.domain.AppointmentSlot;
import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;
import com.smlaurindo.risanailstudio.port.outbound.persistence.AppointmentRepositoryPort;

import com.smlaurindo.risanailstudio.port.outbound.persistence.projection.AppointmentDetailProjection;
import com.smlaurindo.risanailstudio.port.outbound.persistence.projection.AppointmentToListProjection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppointmentRepositoryAdapter implements AppointmentRepositoryPort {

    private final AppointmentJpaRepository appointmentJpaRepository;

    @Override
    public Appointment save(final Appointment appointment) {
        log.debug("Saving appointment: {}", appointment.getId());

        final var entity = AppointmentJpaEntity.fromDomain(appointment);

        final var savedEntity = appointmentJpaRepository.save(entity);

        return savedEntity.toDomain();
    }

    @Override
    public boolean isSlotAvailable(final AppointmentSlot slot) {
        log.debug("Checking availability for appointment slot: {} - {}", slot.startsAt(), slot.endsAt());

        return !appointmentJpaRepository.existsConflict(
                slot.startsAt(),
                slot.endsAt()
        );
    }

    @Override
    public Optional<Appointment> findById(final String id) {
        log.debug("Finding appointment by id: {}", id);

        return appointmentJpaRepository.findById(id)
                .map(AppointmentJpaEntity::toDomain);
    }

    @Override
    public Optional<AppointmentDetailProjection> findDetailById(final String id) {
        log.debug("Finding appointment detail by id: {}", id);

        return appointmentJpaRepository.findDetailById(id)
                .map(entity -> new AppointmentDetailProjection(
                        entity.getId(),
                        entity.getStartsAt(),
                        entity.getEndsAt(),
                        (int) Duration.between(entity.getStartsAt(), entity.getEndsAt()).toMinutes(),
                        entity.getStatus(),
                        entity.getCreatedAt(),
                        entity.getConfirmedAt(),
                        entity.getCancelledAt(),
                        entity.getCustomerId(),
                        entity.getCustomerName(),
                        entity.getCustomerPhoto(),
                        entity.getServiceId(),
                        entity.getServiceName(),
                        entity.getServicePriceCents(),
                        entity.getServiceIcon(),
                        entity.getServiceDurationMinutes()
                ));
    }

    @Override
    public List<AppointmentToListProjection> findAppointments(
            final Instant startDate,
            final Instant endDate,
            final AppointmentStatus status,
            final String searchQuery
    ) {
        log.debug("Finding appointments from {} to {} with status {} and search query '{}'",
                startDate, endDate, status, searchQuery);

        final var entities = appointmentJpaRepository.findAppointments(
                startDate,
                endDate,
                status,
                searchQuery
        );

        return entities.stream()
                .map(entity -> new AppointmentToListProjection(
                        entity.getId(),
                        entity.getCustomerId(),
                        entity.getCustomerName(),
                        entity.getCustomerPhoto(),
                        entity.getServiceId(),
                        entity.getServiceName(),
                        entity.getStatus(),
                        entity.getStartsAt(),
                        entity.getEndsAt()
                ))
                .toList();
    }
}
