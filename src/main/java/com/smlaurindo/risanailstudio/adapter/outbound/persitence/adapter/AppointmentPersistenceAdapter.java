package com.smlaurindo.risanailstudio.adapter.outbound.persitence.adapter;

import com.smlaurindo.risanailstudio.adapter.outbound.persitence.entity.AppointmentJpaEntity;
import com.smlaurindo.risanailstudio.adapter.outbound.persitence.repository.AppointmentJpaRepository;
import com.smlaurindo.risanailstudio.application.domain.Appointment;
import com.smlaurindo.risanailstudio.application.domain.AppointmentSlot;
import com.smlaurindo.risanailstudio.port.outbound.persistence.AppointmentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppointmentPersistenceAdapter implements AppointmentRepository {

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
}
