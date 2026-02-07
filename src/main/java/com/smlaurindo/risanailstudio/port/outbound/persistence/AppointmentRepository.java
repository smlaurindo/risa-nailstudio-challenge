package com.smlaurindo.risanailstudio.port.outbound.persistence;

import com.smlaurindo.risanailstudio.application.domain.Appointment;
import com.smlaurindo.risanailstudio.application.domain.AppointmentSlot;

import java.util.Optional;

public interface AppointmentRepository {
    Appointment save(Appointment appointment);
    boolean isSlotAvailable(AppointmentSlot slot);
    Optional<Appointment> findById(String id);
}
