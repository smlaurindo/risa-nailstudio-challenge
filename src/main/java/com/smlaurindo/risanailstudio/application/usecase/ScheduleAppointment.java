package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface ScheduleAppointment {
    record ScheduleAppointmentInput(
            String credentialsId,
            String customerName,
            String serviceId,
            LocalDate scheduledDate,
            LocalTime scheduledTime
    ) {}

    record ScheduleAppointmentOutput(
            String appointmentId,
            String customerId,
            String serviceId,
            LocalDateTime startsAt,
            LocalDateTime endsAt,
            AppointmentStatus appointmentStatus
    ) {}

    ScheduleAppointmentOutput scheduleAppointment(ScheduleAppointmentInput input);
}
