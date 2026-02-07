package com.smlaurindo.risanailstudio.adapter.outbound.persitence.repository.projection;

import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;

import java.time.Instant;

public interface AppointmentDetailProjection {
    String getId();
    Instant getStartsAt();
    Instant getEndsAt();
    AppointmentStatus getStatus();
    Instant getCreatedAt();
    Instant getConfirmedAt();
    Instant getCancelledAt();
    String getCustomerId();
    String getCustomerName();
    String getCustomerPhoto();
    String getServiceId();
    String getServiceName();
    Integer getServicePriceCents();
    String getServiceIcon();
    Integer getServiceDurationMinutes();
}
