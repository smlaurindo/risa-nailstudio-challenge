package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response;

import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;
import com.smlaurindo.risanailstudio.application.usecase.GetAppointment;

import java.time.Instant;

public record GetAppointmentResponse(
        String id,
        Instant startsAt,
        Instant endsAt,
        int durationMinutes,
        AppointmentStatus status,
        Instant createdAt,
        Instant confirmedAt,
        Instant cancelledAt,
        CustomerInfo customer,
        ServiceInfo service
) {
    public record CustomerInfo(
            String id,
            String name,
            String photo
    ) {}

    public record ServiceInfo(
            String id,
            String name,
            int priceCents,
            String icon,
            int durationMinutes
    ) {}

    public static GetAppointmentResponse fromOutput(GetAppointment.GetAppointmentOutput output) {
        return new GetAppointmentResponse(
                output.id(),
                output.startsAt(),
                output.endsAt(),
                output.durationMinutes(),
                output.status(),
                output.createdAt(),
                output.confirmedAt(),
                output.cancelledAt(),
                new CustomerInfo(
                        output.customer().id(),
                        output.customer().name(),
                        output.customer().photo()
                ),
                new ServiceInfo(
                        output.service().id(),
                        output.service().name(),
                        output.service().priceCents(),
                        output.service().icon(),
                        output.service().durationMinutes()
                )
        );
    }
}
