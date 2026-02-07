package com.smlaurindo.risanailstudio.port.outbound.persistence.projection;

import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;

import java.time.Instant;

public record AppointmentDetailProjection(
        String id,
        Instant startsAt,
        Instant endsAt,
        int durationMinutes,
        AppointmentStatus status,
        Instant createdAt,
        Instant confirmedAt,
        Instant cancelledAt,
        String customerId,
        String customerName,
        String customerPhoto,
        String serviceId,
        String serviceName,
        int servicePriceCents,
        String serviceIcon,
        int serviceDurationMinutes
) {}
