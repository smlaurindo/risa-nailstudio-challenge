package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request;

import com.smlaurindo.risanailstudio.application.usecase.ScheduleAppointment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.Instant;

@Schema(description = "Data for scheduling a service")
public record ScheduleAppointmentRequest(
        @Schema(
                description = "Customer name (optional, if not provided uses the registered name)",
                example = "John Doe",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                minLength = 2,
                maxLength = 100
        )
        @Size(min = 2, max = 100)
        String customerName,

        @Schema(
                description = "Service ID to be scheduled",
                example = "123e4567-e89b-12d3-a456-426614174000",
                requiredMode = Schema.RequiredMode.REQUIRED,
                type = "string",
                format = "uuid"
        )
        @NotBlank()
        String serviceId,

        @Schema(
                description = "Appointment date and time (ISO 8601 format, must be in the future)",
                example = "2026-02-15T14:30:00Z",
                requiredMode = Schema.RequiredMode.REQUIRED,
                type = "string",
                format = "date-time"
        )
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