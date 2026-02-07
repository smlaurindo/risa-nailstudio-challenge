package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response;

import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;
import com.smlaurindo.risanailstudio.application.usecase.ListAppointments;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(description = "Appointments list item")
public record ListAppointmentsResponse(
        @Schema(description = "Appointment Id", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,
        
        @Schema(description = "Customer information")
        CustomerInfo customer,
        
        @Schema(description = "Service information")
        ServiceInfo service,
        
        @Schema(description = "Appointment status", example = "PENDING")
        AppointmentStatus status,
        
        @Schema(description = "Start date and time", example = "2026-02-15T14:30:00Z")
        Instant startsAt,
        
        @Schema(description = "End date and time", example = "2026-02-15T15:30:00Z")
        Instant endsAt
) {
    @Schema(description = "Customer information")
    public record CustomerInfo(
            @Schema(description = "Customer Id", example = "123e4567-e89b-12d3-a456-426614174001")
            String id,
            
            @Schema(description = "Customer name", example = "Maria Silva")
            String name,
            
            @Schema(description = "Customer photo URL", example = "https://example.com/photos/maria_silva.jpg")
            String photo
    ) {}

    @Schema(description = "Service information")
    public record ServiceInfo(
            @Schema(description = "Service Id", example = "123e4567-e89b-12d3-a456-426614174002")
            String id,
            
            @Schema(description = "Service name", example = "Manicure")
            String name
    ) {}

    public static List<ListAppointmentsResponse> from(
            List<ListAppointments.ListAppointmentsOutput> outputs
    ) {
        return outputs.stream()
                .map(output -> new ListAppointmentsResponse(
                        output.id(),
                        new CustomerInfo(
                                output.customerId(),
                                output.customerName(),
                                output.customerPhoto()
                        ),
                        new ServiceInfo(
                                output.serviceId(),
                                output.serviceName()
                        ),
                        output.status(),
                        output.startsAt(),
                        output.endsAt()
                ))
                .toList();
    }
}
