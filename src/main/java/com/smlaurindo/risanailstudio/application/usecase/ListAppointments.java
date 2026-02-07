package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public interface ListAppointments {
    record ListAppointmentsInput(
            LocalDate startDate,
            LocalDate endDate,
            AppointmentStatus status,
            String searchQuery,
            String credentialsId
    ) {}

    record ListAppointmentsOutput(
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

    List<ListAppointmentsOutput> listAppointments(ListAppointmentsInput input);
}
