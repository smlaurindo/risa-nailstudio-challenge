package com.smlaurindo.risanailstudio.adapter.outbound.persitence.repository.projection;

import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;

import java.time.Instant;

public interface AppointmentToListProjection {
    String getId();
    String getCustomerId();
    String getCustomerName();
    String getCustomerPhoto();
    String getServiceId();
    String getServiceName();
    AppointmentStatus getStatus();
    Instant getStartsAt();
    Instant getEndsAt();
}
