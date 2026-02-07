package com.smlaurindo.risanailstudio.adapter.outbound.persitence.adapter;

import com.smlaurindo.risanailstudio.adapter.outbound.persitence.entity.AppointmentJpaEntity;
import com.smlaurindo.risanailstudio.adapter.outbound.persitence.repository.AppointmentJpaRepository;
import com.smlaurindo.risanailstudio.application.domain.Appointment;
import com.smlaurindo.risanailstudio.application.domain.AppointmentSlot;
import com.smlaurindo.risanailstudio.port.outbound.persistence.AppointmentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentPersistenceAdapter implements AppointmentRepository {

    private final AppointmentJpaRepository appointmentJpaRepository;

    @Override
    public Appointment save(final Appointment appointment) {
        final var entity = AppointmentJpaEntity.fromDomain(appointment);

        final var savedEntity = appointmentJpaRepository.save(entity);

        return savedEntity.toDomain();
    }

    @Override
    public boolean isSlotAvailable(final AppointmentSlot slot) {
        return !appointmentJpaRepository.existsConflict(
                slot.startsAt(),
                slot.endsAt()
        );
    }
}
