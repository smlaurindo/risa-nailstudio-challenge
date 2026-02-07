package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response;

import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;
import com.smlaurindo.risanailstudio.application.usecase.ScheduleAppointment;

import java.time.LocalDateTime;

public record ScheduleAppointmentResponse(
        String id,
        String customerId,
        String serviceId,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        AppointmentStatus appointmentStatus
) {
    public static ScheduleAppointmentResponse fromOutput(
            final ScheduleAppointment.ScheduleAppointmentOutput output
    ) {
        return new ScheduleAppointmentResponse(
                output.appointmentId(),
                output.customerId(),
                output.serviceId(),
                output.startsAt(),
                output.endsAt(),
                output.appointmentStatus()
        );
    }
}
