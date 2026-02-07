package com.smlaurindo.risanailstudio.port.outbound.persistence;

import com.smlaurindo.risanailstudio.application.domain.Appointment;
import com.smlaurindo.risanailstudio.application.domain.AppointmentSlot;

public interface AppointmentRepository {
    Appointment save(Appointment appointment);
    boolean isSlotAvailable(AppointmentSlot slot);
}
