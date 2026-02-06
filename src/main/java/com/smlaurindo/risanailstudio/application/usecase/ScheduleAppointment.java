package com.smlaurindo.risanailstudio.application.usecase;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ScheduleAppointment {
    record ScheduleAppointmentInput(
            String customerId,
            String customerName,
            String serviceId,
            LocalDate scheduledDate,
            LocalTime scheduledTime
    ) {}

    record ScheduleAppointmentOutput(
            String appointmentId,
            String customerId,
            String serviceId,
            LocalDate scheduledDate,
            LocalTime scheduledTime
    ) {}

    ScheduleAppointmentOutput scheduleAppointment(ScheduleAppointmentInput input);
}
