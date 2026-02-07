package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.port.outbound.persistence.AdminRepository;
import com.smlaurindo.risanailstudio.port.outbound.persistence.AppointmentRepository;
import com.smlaurindo.risanailstudio.application.exception.AuthorizationException;
import com.smlaurindo.risanailstudio.application.exception.ErrorCode;
import com.smlaurindo.risanailstudio.application.exception.NotFoundException;

public class CancelAppointmentUseCase implements CancelAppointment {

    private final AdminRepository adminRepository;
    private final AppointmentRepository appointmentRepository;

    public CancelAppointmentUseCase(AdminRepository adminRepository, AppointmentRepository appointmentRepository) {
        this.adminRepository = adminRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public CancelAppointmentOutput cancelAppointment(CancelAppointmentInput input) {
        adminRepository.findByCredentialsId(input.credentialsId())
                .orElseThrow(() -> new AuthorizationException(ErrorCode.INSUFFICIENT_PRIVILEGES));

        var appointment = appointmentRepository.findById(input.appointmentId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.APPOINTMENT_NOT_FOUND));

        appointment.cancel();

        appointmentRepository.save(appointment);

        return new CancelAppointmentOutput(
                appointment.getId(),
                appointment.getCustomerId(),
                appointment.getServiceId(),
                appointment.getSlot().startsAt(),
                appointment.getSlot().endsAt(),
                appointment.getStatus(),
                appointment.getCancelledAt()
        );
    }
}
