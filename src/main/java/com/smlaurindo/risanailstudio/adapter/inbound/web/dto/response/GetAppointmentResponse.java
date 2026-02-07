package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response;

import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;
import com.smlaurindo.risanailstudio.application.usecase.GetAppointment;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Complete appointment details")
public record GetAppointmentResponse(
        @Schema(description = "Appointment Id", example = "123e4567-e89b-12d3-a456-426614174000", type = "string", format = "uuid")
        String id,
        
        @Schema(description = "Start date and time", example = "2026-02-15T14:30:00Z", type = "string", format = "date-time")
        Instant startsAt,
        
        @Schema(description = "End date and time", example = "2026-02-15T15:30:00Z", type = "string", format = "date-time")
        Instant endsAt,
        
        @Schema(description = "Duration in minutes", example = "60", type = "integer")
        int durationMinutes,
        
        @Schema(description = "Appointment status", example = "PENDING")
        AppointmentStatus status,
        
        @Schema(description = "Creation date and time", example = "2026-02-01T10:00:00Z", type = "string", format = "date-time")
        Instant createdAt,
        
        @Schema(description = "Confirmation date and time (null if not confirmed yet)", type = "string", format = "date-time")
        Instant confirmedAt,
        
        @Schema(description = "Cancellation date and time (null if not cancelled yet)", type = "string", format = "date-time")
        Instant cancelledAt,
        
        @Schema(description = "Customer information")
        CustomerInfo customer,
        
        @Schema(description = "Service information")
        ServiceInfo service
) {
    @Schema(description = "Customer information")
    public record CustomerInfo(
            @Schema(description = "Customer Id", example = "123e4567-e89b-12d3-a456-426614174001", type = "string", format = "uuid")
            String id,
            
            @Schema(description = "Customer name", example = "John Doe")
            String name,
            
            @Schema(description = "Customer photo URL", example = "https://example.com/photos/john_doe.jpg", type = "string", format = "uri")
            String photo
    ) {}

    @Schema(description = "Service information")
    public record ServiceInfo(
            @Schema(description = "Service Id", example = "123e4567-e89b-12d3-a456-426614174002", type = "string", format = "uuid")
            String id,
            
            @Schema(description = "Service name", example = "Manicure")
            String name,
            
            @Schema(description = "Price in cents", example = "5000", type = "integer")
            int priceCents,
            
            @Schema(description = "Service icon", example = "NAIL")
            String icon,
            
            @Schema(description = "Duration in minutes", example = "60", type = "integer")
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
