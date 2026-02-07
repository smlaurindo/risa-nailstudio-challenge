package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.application.exception.AuthorizationException;
import com.smlaurindo.risanailstudio.application.exception.ErrorCode;
import com.smlaurindo.risanailstudio.application.exception.NotFoundException;
import com.smlaurindo.risanailstudio.port.outbound.persistence.AdminRepository;
import com.smlaurindo.risanailstudio.port.outbound.persistence.AppointmentRepository;

public class GetAppointmentUseCase implements GetAppointment {

    private final AdminRepository adminRepository;
    private final AppointmentRepository appointmentRepository;

    public GetAppointmentUseCase(AdminRepository adminRepository, AppointmentRepository appointmentRepository) {
        this.adminRepository = adminRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public GetAppointmentOutput getAppointment(GetAppointmentInput input) {
        adminRepository.findByCredentialsId(input.credentialsId())
                .orElseThrow(() -> new AuthorizationException(ErrorCode.INSUFFICIENT_PRIVILEGES));

        var appointment = appointmentRepository.findDetailById(input.appointmentId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.APPOINTMENT_NOT_FOUND));

        return new GetAppointmentOutput(
                appointment.id(),
                appointment.startsAt(),
                appointment.endsAt(),
                appointment.durationMinutes(),
                appointment.status(),
                appointment.createdAt(),
                appointment.confirmedAt(),
                appointment.cancelledAt(),
                new GetAppointmentOutput.CustomerInfo(
                        appointment.customerId(),
                        appointment.customerName(),
                        appointment.customerPhoto()
                ),
                new GetAppointmentOutput.ServiceInfo(
                        appointment.serviceId(),
                        appointment.serviceName(),
                        appointment.servicePriceCents(),
                        appointment.serviceIcon(),
                        appointment.serviceDurationMinutes()
                )
        );
    }
}
