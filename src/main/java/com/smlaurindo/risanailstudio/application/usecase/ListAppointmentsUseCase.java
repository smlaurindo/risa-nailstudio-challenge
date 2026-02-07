package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.port.outbound.persistence.AdminRepository;
import com.smlaurindo.risanailstudio.port.outbound.persistence.AppointmentRepository;
import com.smlaurindo.risanailstudio.shared.exception.AuthorizationException;
import com.smlaurindo.risanailstudio.shared.exception.BusinessRuleException;
import com.smlaurindo.risanailstudio.shared.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ListAppointmentsUseCase implements ListAppointments {

    private final AdminRepository adminRepository;
    private final AppointmentRepository appointmentRepository;

    public ListAppointmentsUseCase(AdminRepository adminRepository, AppointmentRepository appointmentRepository) {
        this.adminRepository = adminRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public List<ListAppointmentsOutput> listAppointments(ListAppointmentsInput input) {
        adminRepository.findByCredentialsId(input.credentialsId())
                .orElseThrow(() -> new AuthorizationException(ErrorCode.INSUFFICIENT_PRIVILEGES));

        if (input.startDate().isAfter(input.endDate())) {
            throw new BusinessRuleException(ErrorCode.DATE_RANGE_INVALID);
        }

        long days = ChronoUnit.DAYS.between(input.startDate(), input.endDate());

        if (days > 31) {
            throw new BusinessRuleException(ErrorCode.DATE_RANGE_TOO_LONG);
        }

        var appointments = appointmentRepository.findAppointments(
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
