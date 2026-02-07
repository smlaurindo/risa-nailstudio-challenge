package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request;

import com.smlaurindo.risanailstudio.application.usecase.ScheduleAppointment;
import jakarta.validation.constraints.*;

import java.time.Instant;

public record ScheduleAppointmentRequest(
        @Size(min = 2, max = 100)
        String customerName,

        @NotBlank()
        String serviceId,

        @NotNull()
        @Future()
        Instant scheduledAt
) {
    public ScheduleAppointmentRequest {
        if (customerName != null) {
            customerName = customerName.trim();
        }
        if (serviceId != null) {
            serviceId = serviceId.trim();
        }
    }

    public ScheduleAppointment.ScheduleAppointmentInput toInput(String credentialsId) {
        return new ScheduleAppointment.ScheduleAppointmentInput(
                credentialsId,
                customerName,
                serviceId,
                scheduledAt
        );
    }
}