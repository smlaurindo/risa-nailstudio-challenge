package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response;

import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;
import com.smlaurindo.risanailstudio.application.usecase.ListAppointments;

import java.time.Instant;
import java.util.List;

public record ListAppointmentsResponse(
        String id,
        CustomerInfo customer,
        ServiceInfo service,
        AppointmentStatus status,
        Instant startsAt,
        Instant endsAt
) {
    public record CustomerInfo(
            String id,
            String name,
            String photo
    ) {}

    public record ServiceInfo(
            String id,
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
