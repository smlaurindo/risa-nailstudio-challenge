package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;

import java.time.Instant;

public interface ScheduleAppointment {
    record ScheduleAppointmentInput(
            String credentialsId,
            String customerName,
            String serviceId,
            Instant scheduledAt
    ) {}

    record ScheduleAppointmentOutput(
            String appointmentId,
            String customerId,
            String serviceId,
            Instant startsAt,
            Instant endsAt,
            AppointmentStatus appointmentStatus
    ) {}

    ScheduleAppointmentOutput scheduleAppointment(ScheduleAppointmentInput input);
}
