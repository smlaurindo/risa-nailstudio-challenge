package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.application.domain.Appointment;
import com.smlaurindo.risanailstudio.application.domain.AppointmentSlot;
import com.smlaurindo.risanailstudio.port.outbound.persistence.AppointmentRepository;
import com.smlaurindo.risanailstudio.port.outbound.persistence.CustomerRepository;
import com.smlaurindo.risanailstudio.port.outbound.persistence.ServiceRepository;

public class ScheduleAppointmentUseCase implements ScheduleAppointment {

    private final CustomerRepository customerRepository;
    private final ServiceRepository serviceRepository;
    private final AppointmentRepository appointmentRepository;

    public ScheduleAppointmentUseCase(CustomerRepository customerRepository, ServiceRepository serviceRepository, AppointmentRepository appointmentRepository) {
        this.customerRepository = customerRepository;
        this.serviceRepository = serviceRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public ScheduleAppointmentOutput scheduleAppointment(ScheduleAppointmentInput input) {
        var customer = customerRepository.findById(input.customerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + input.customerId()));

        if (customer.getName() == null && input.customerName() != null) {
            customer.setName(input.customerName());
            customerRepository.save(customer);
        }

        var service = serviceRepository.findById(input.serviceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found with id: " + input.serviceId()));

        var slot = AppointmentSlot.from(
                input.scheduledTime(),
                input.scheduledDate(),
                service.getDurationMinutes()
        );

        if (!appointmentRepository.isSlotAvailable(slot)) {
            throw new IllegalStateException("Appointment slot not available");
        }

        var appointment = Appointment.schedule(
                input.serviceId(),
                input.customerId(),
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
