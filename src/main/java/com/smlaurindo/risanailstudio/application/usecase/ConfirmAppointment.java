package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;

import java.time.Instant;
import java.time.LocalDateTime;

public interface ConfirmAppointment {
    record ConfirmAppointmentInput(
            String appointmentId,
            String credentialsId
    ) {}

    record ConfirmAppointmentOutput(
            String appointmentId,
            String customerId,
            String serviceId,
            LocalDateTime startsAt,
            LocalDateTime endsAt,
            AppointmentStatus appointmentStatus,
            Instant confirmedAt
    ) {}

    ConfirmAppointmentOutput confirmAppointment(ConfirmAppointmentInput input);
}
