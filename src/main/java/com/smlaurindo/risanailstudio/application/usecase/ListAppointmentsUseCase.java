package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.port.outbound.persistence.AdminRepositoryPort;
import com.smlaurindo.risanailstudio.port.outbound.persistence.AppointmentRepositoryPort;
import com.smlaurindo.risanailstudio.application.exception.AuthorizationException;
import com.smlaurindo.risanailstudio.application.exception.BusinessRuleException;
import com.smlaurindo.risanailstudio.application.exception.ErrorCode;

import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ListAppointmentsUseCase implements ListAppointments {

    private final AdminRepositoryPort adminRepositoryPort;
    private final AppointmentRepositoryPort appointmentRepositoryPort;

    public ListAppointmentsUseCase(AdminRepositoryPort adminRepositoryPort, AppointmentRepositoryPort appointmentRepositoryPort) {
        this.adminRepositoryPort = adminRepositoryPort;
        this.appointmentRepositoryPort = appointmentRepositoryPort;
    }

    @Override
    public List<ListAppointmentsOutput> listAppointments(ListAppointmentsInput input) {
        adminRepositoryPort.findByCredentialsId(input.credentialsId())
                .orElseThrow(() -> new AuthorizationException(ErrorCode.INSUFFICIENT_PRIVILEGES));

        if (input.startDate().isAfter(input.endDate())) {
            throw new BusinessRuleException(ErrorCode.DATE_RANGE_INVALID);
        }

        long days = ChronoUnit.DAYS.between(input.startDate(), input.endDate());

        if (days > 31) {
            throw new BusinessRuleException(ErrorCode.DATE_RANGE_TOO_LONG);
        }

        var appointments = appointmentRepositoryPort.findAppointments(
                input.startDate().atStartOfDay(ZoneOffset.UTC).toInstant(),
                input.endDate().plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant(),
                input.status(),
                input.searchQuery()
        );

        return appointments.stream()
                .map(appointment -> new ListAppointmentsOutput(
                        appointment.id(),
                        appointment.customerId(),
                        appointment.customerName(),
                        appointment.customerPhoto(),
                        appointment.serviceId(),
                        appointment.serviceName(),
                        appointment.status(),
                        appointment.startsAt(),
                        appointment.endsAt()
                )).toList();
    }
}
