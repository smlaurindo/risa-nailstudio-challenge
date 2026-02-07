package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response;

import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;
import com.smlaurindo.risanailstudio.application.usecase.ConfirmAppointment;

import java.time.Instant;
import java.time.LocalDateTime;

public record ConfirmAppointmentResponse(
        String id,
        String customerId,
        String serviceId,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        AppointmentStatus appointmentStatus,
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
