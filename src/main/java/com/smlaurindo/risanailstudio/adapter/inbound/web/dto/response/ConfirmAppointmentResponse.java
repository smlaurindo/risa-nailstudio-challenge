package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response;

import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;
import com.smlaurindo.risanailstudio.application.usecase.ConfirmAppointment;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Confirmed appointment response")
public record ConfirmAppointmentResponse(
        @Schema(description = "Appointment Id", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,
        
        @Schema(description = "Customer Id", example = "123e4567-e89b-12d3-a456-426614174001")
        String customerId,
        
        @Schema(description = "Service Id", example = "123e4567-e89b-12d3-a456-426614174002")
        String serviceId,
        
        @Schema(description = "Start date and time", example = "2026-02-15T14:30:00Z")
        Instant startsAt,
        
        @Schema(description = "End date and time", example = "2026-02-15T15:30:00Z")
        Instant endsAt,
        
        @Schema(description = "Appointment status", example = "CONFIRMED")
        AppointmentStatus appointmentStatus,
        
        @Schema(description = "Confirmation date and time", example = "2026-02-10T16:00:00Z")
        Instant confirmedAt
) {
    public static ConfirmAppointmentResponse fromOutput(
            final ConfirmAppointment.ConfirmAppointmentOutput output
    ) {
        return new ConfirmAppointmentResponse(
                output.appointmentId(),
                output.customerId(),
                output.serviceId(),
                output.startsAt(),
                output.endsAt(),
                output.appointmentStatus(),
                output.confirmedAt()
        );
    }
}
