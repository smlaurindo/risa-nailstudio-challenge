package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request;

import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Filter for appointment status")
public enum AppointmentStatusFilter {
    @Schema(description = "All statuses")
    ALL,
    
    @Schema(description = "Pending appointments only")
    PENDING,
    
    @Schema(description = "Confirmed appointments only")
    CONFIRMED,
    
    @Schema(description = "Cancelled appointments only")
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
