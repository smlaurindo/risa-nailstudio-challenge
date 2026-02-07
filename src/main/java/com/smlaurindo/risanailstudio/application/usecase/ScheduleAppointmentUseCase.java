package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.application.domain.Appointment;
import com.smlaurindo.risanailstudio.application.domain.AppointmentSlot;
import com.smlaurindo.risanailstudio.port.outbound.persistence.AppointmentRepository;
import com.smlaurindo.risanailstudio.port.outbound.persistence.CustomerRepository;
import com.smlaurindo.risanailstudio.port.outbound.persistence.ServiceRepository;
import com.smlaurindo.risanailstudio.shared.exception.BusinessRuleException;
import com.smlaurindo.risanailstudio.shared.exception.ConflictException;
import com.smlaurindo.risanailstudio.shared.exception.ErrorCode;
import com.smlaurindo.risanailstudio.shared.exception.NotFoundException;

public class ScheduleAppointmentUseCase implements ScheduleAppointment {

    private final CustomerRepository customerRepository;
    private final ServiceRepository serviceRepository;
    private final AppointmentRepository appointmentRepository;

    public ScheduleAppointmentUseCase(
            CustomerRepository customerRepository,
            ServiceRepository serviceRepository,
            AppointmentRepository appointmentRepository
    ) {
        this.customerRepository = customerRepository;
        this.serviceRepository = serviceRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public ScheduleAppointmentOutput scheduleAppointment(ScheduleAppointmentInput input) {
        var customer = customerRepository.findByCredentialsId(input.credentialsId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));

        if (customer.getName() == null && input.customerName() == null) {
            throw new BusinessRuleException(ErrorCode.CUSTOMER_NAME_REQUIRED, "customerName");
        }

        if (customer.getName() == null && input.customerName() != null) {
            customer.setName(input.customerName());
            customerRepository.save(customer);
        }

        var service = serviceRepository.findById(input.serviceId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SERVICE_NOT_FOUND));

        var slot = AppointmentSlot.from(
                input.scheduledAt(),
                service.getDurationMinutes()
        );

        if (!appointmentRepository.isSlotAvailable(slot)) {
            throw new ConflictException(ErrorCode.APPOINTMENT_SLOT_UNAVAILABLE, "scheduledAt");
        }

        var appointment = Appointment.schedule(
                customer.getId(),
                input.serviceId(),
                slot
        );

        appointmentRepository.save(appointment);

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
