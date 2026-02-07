package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;

import java.time.Instant;

public interface GetAppointment {
    record GetAppointmentInput(
            String appointmentId,
            String credentialsId
    ) {}

    record GetAppointmentOutput(
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
    }

    GetAppointmentOutput getAppointment(GetAppointmentInput input);
}
