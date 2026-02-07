package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request;

import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;

public enum AppointmentStatusFilter {
    ALL,
    PENDING,
    CONFIRMED,
    CANCELLED;

    public AppointmentStatus toAppointmentStatus() {
        return switch (this) {
            case ALL -> null;
            case PENDING -> AppointmentStatus.PENDING;
            case CONFIRMED -> AppointmentStatus.CONFIRMED;
            case CANCELLED -> AppointmentStatus.CANCELLED;
        };
    }
}
