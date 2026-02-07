package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;

import java.time.Instant;

public interface CancelAppointment {
    record CancelAppointmentInput(
            String appointmentId,
            String credentialsId
    ) {}

    record CancelAppointmentOutput(
            String appointmentId,
            String customerId,
            String serviceId,
            Instant startsAt,
            Instant endsAt,
            AppointmentStatus appointmentStatus,
            Instant cancelledAt
    ) {}

    CancelAppointmentOutput cancelAppointment(CancelAppointmentInput input);
}
