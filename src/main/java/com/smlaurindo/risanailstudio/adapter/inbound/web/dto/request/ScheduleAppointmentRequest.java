package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request;

import com.smlaurindo.risanailstudio.application.usecase.ScheduleAppointment;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleAppointmentRequest(
        String customerId,
        String customerName,
        String serviceId,
        LocalDate scheduledDate,
        LocalTime scheduledTime
) {
    public ScheduleAppointment.ScheduleAppointmentInput toInput() {
        return new ScheduleAppointment.ScheduleAppointmentInput(
                customerId,
                customerName,
                serviceId,
                scheduledDate,
                scheduledTime
        );
    }
}