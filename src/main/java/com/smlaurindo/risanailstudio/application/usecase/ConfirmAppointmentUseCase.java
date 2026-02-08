package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.port.outbound.persistence.AdminRepositoryPort;
import com.smlaurindo.risanailstudio.port.outbound.persistence.AppointmentRepositoryPort;
import com.smlaurindo.risanailstudio.application.exception.AuthorizationException;
import com.smlaurindo.risanailstudio.application.exception.ErrorCode;
import com.smlaurindo.risanailstudio.application.exception.NotFoundException;

public class ConfirmAppointmentUseCase implements ConfirmAppointment {

    private final AdminRepositoryPort adminRepositoryPort;
    private final AppointmentRepositoryPort appointmentRepositoryPort;

    public ConfirmAppointmentUseCase(AdminRepositoryPort adminRepositoryPort, AppointmentRepositoryPort appointmentRepositoryPort) {
        this.adminRepositoryPort = adminRepositoryPort;
        this.appointmentRepositoryPort = appointmentRepositoryPort;
    }

    @Override
    public ConfirmAppointmentOutput confirmAppointment(ConfirmAppointmentInput input) {
        adminRepositoryPort.findByCredentialsId(input.credentialsId())
                .orElseThrow(() -> new AuthorizationException(ErrorCode.INSUFFICIENT_PRIVILEGES));

        var appointment = appointmentRepositoryPort.findById(input.appointmentId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.APPOINTMENT_NOT_FOUND));

        appointment.confirm();

        appointmentRepositoryPort.save(appointment);

        return new ConfirmAppointmentOutput(
                appointment.getId(),
                appointment.getCustomerId(),
                appointment.getServiceId(),
                appointment.getSlot().startsAt(),
                appointment.getSlot().endsAt(),
                appointment.getStatus(),
                appointment.getConfirmedAt()
        );
    }
}
