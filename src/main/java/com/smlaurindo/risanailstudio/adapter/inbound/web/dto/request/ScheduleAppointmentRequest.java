package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request;

import com.smlaurindo.risanailstudio.application.usecase.ScheduleAppointment;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleAppointmentRequest(
        @Size(min = 2, max = 100)
        String customerName,

        @NotBlank()
        String serviceId,

        @NotNull()
        @FutureOrPresent()
        LocalDate scheduledDate,

        @NotNull()
        LocalTime scheduledTime
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
                scheduledDate,
                scheduledTime
        );
    }
}