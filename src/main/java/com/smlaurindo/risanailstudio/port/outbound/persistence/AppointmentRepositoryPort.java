package com.smlaurindo.risanailstudio.port.outbound.persistence;

import com.smlaurindo.risanailstudio.application.domain.Appointment;
import com.smlaurindo.risanailstudio.application.domain.AppointmentSlot;
import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;
import com.smlaurindo.risanailstudio.port.outbound.persistence.projection.AppointmentDetailProjection;
import com.smlaurindo.risanailstudio.port.outbound.persistence.projection.AppointmentToListProjection;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepositoryPort {
    Appointment save(Appointment appointment);
    boolean isSlotAvailable(AppointmentSlot slot);
    Optional<Appointment> findById(String id);
    Optional<AppointmentDetailProjection> findDetailById(String id);
    List<AppointmentToListProjection> findAppointments(
            Instant startDate,
            Instant endDate,
            AppointmentStatus status,
            String searchQuery
    );
}
