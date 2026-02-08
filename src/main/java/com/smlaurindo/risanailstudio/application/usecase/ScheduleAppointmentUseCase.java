package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.application.domain.Appointment;
import com.smlaurindo.risanailstudio.application.domain.AppointmentSlot;
import com.smlaurindo.risanailstudio.port.outbound.persistence.AppointmentRepositoryPort;
import com.smlaurindo.risanailstudio.port.outbound.persistence.CustomerRepositoryPort;
import com.smlaurindo.risanailstudio.port.outbound.persistence.ServiceRepositoryPort;
import com.smlaurindo.risanailstudio.application.exception.BusinessRuleException;
import com.smlaurindo.risanailstudio.application.exception.ConflictException;
import com.smlaurindo.risanailstudio.application.exception.ErrorCode;
import com.smlaurindo.risanailstudio.application.exception.NotFoundException;

public class ScheduleAppointmentUseCase implements ScheduleAppointment {

    private final CustomerRepositoryPort customerRepositoryPort;
    private final ServiceRepositoryPort serviceRepositoryPort;
    private final AppointmentRepositoryPort appointmentRepositoryPort;

    public ScheduleAppointmentUseCase(
            CustomerRepositoryPort customerRepositoryPort,
            ServiceRepositoryPort serviceRepositoryPort,
            AppointmentRepositoryPort appointmentRepositoryPort
    ) {
        this.customerRepositoryPort = customerRepositoryPort;
        this.serviceRepositoryPort = serviceRepositoryPort;
        this.appointmentRepositoryPort = appointmentRepositoryPort;
    }

    @Override
    public ScheduleAppointmentOutput scheduleAppointment(ScheduleAppointmentInput input) {
        var customer = customerRepositoryPort.findByCredentialsId(input.credentialsId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));

        if (customer.getName() == null && input.customerName() == null) {
            throw new BusinessRuleException(ErrorCode.CUSTOMER_NAME_REQUIRED, "customerName");
        }

        if (customer.getName() == null && input.customerName() != null) {
            customer.setName(input.customerName());
            customerRepositoryPort.save(customer);
        }

        var service = serviceRepositoryPort.findById(input.serviceId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SERVICE_NOT_FOUND));

        var slot = AppointmentSlot.from(
                input.scheduledAt(),
                service.getDurationMinutes()
        );

        if (!appointmentRepositoryPort.isSlotAvailable(slot)) {
            throw new ConflictException(ErrorCode.APPOINTMENT_SLOT_UNAVAILABLE, "scheduledAt");
        }

        var appointment = Appointment.schedule(
                customer.getId(),
                input.serviceId(),
                slot
        );

        appointmentRepositoryPort.save(appointment);

        return new ScheduleAppointmentOutput(
                appointment.getId(),
                appointment.getCustomerId(),
                appointment.getServiceId(),
                appointment.getSlot().startsAt(),
                appointment.getSlot().endsAt(),
                appointment.getStatus()
        );
    }
}
