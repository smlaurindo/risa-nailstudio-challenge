package com.smlaurindo.risanailstudio.port.outbound.persistence.projection;

import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;

import java.time.Instant;

public record AppointmentToListProjection(
        String id,
        String customerId,
        String customerName,
        String customerPhoto,
        String serviceId,
        String serviceName,
        AppointmentStatus status,
        Instant startsAt,
        Instant endsAt
) {}
