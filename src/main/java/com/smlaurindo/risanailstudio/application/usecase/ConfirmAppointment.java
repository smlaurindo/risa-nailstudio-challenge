package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;

import java.time.Instant;

public interface ConfirmAppointment {
    record ConfirmAppointmentInput(
            String appointmentId,
            String credentialsId
    ) {}

    record ConfirmAppointmentOutput(
            String appointmentId,
            String customerId,
            String serviceId,
            Instant startsAt,
            Instant endsAt,
            AppointmentStatus appointmentStatus,
            Instant confirmedAt
    ) {}

    ConfirmAppointmentOutput confirmAppointment(ConfirmAppointmentInput input);
}
