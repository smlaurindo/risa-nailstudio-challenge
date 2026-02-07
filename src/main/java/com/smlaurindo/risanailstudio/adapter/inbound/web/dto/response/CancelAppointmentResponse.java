package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response;

import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;
import com.smlaurindo.risanailstudio.application.usecase.CancelAppointment;

import java.time.Instant;

public record CancelAppointmentResponse(
        String id,
        String customerId,
        String serviceId,
        Instant startsAt,
        Instant endsAt,
        AppointmentStatus appointmentStatus,
        Instant cancelledAt
) {
    public static CancelAppointmentResponse fromOutput(
            final CancelAppointment.CancelAppointmentOutput output
    ) {
        return new CancelAppointmentResponse(
                output.appointmentId(),
                output.customerId(),
                output.serviceId(),
                output.startsAt(),
                output.endsAt(),
                output.appointmentStatus(),
                output.cancelledAt()
        );
    }
}
