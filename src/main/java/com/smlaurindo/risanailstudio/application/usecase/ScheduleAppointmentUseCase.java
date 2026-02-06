package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.port.outbound.persistence.CustomerRepository;
import com.smlaurindo.risanailstudio.port.outbound.persistence.ServiceRepository;

public class ScheduleAppointmentUseCase implements ScheduleAppointment {

    private final CustomerRepository customerRepository;
    private final ServiceRepository serviceRepository;

    public ScheduleAppointmentUseCase(CustomerRepository customerRepository, ServiceRepository serviceRepository) {
        this.customerRepository = customerRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public ScheduleAppointmentOutput scheduleAppointment(ScheduleAppointmentInput input) {
        var customer = customerRepository.findById(input.customerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + input.customerId()));

        if (customer.getName() == null) {
            customer.setName(input.customerName());
            customerRepository.save(customer);
        }
        var service = serviceRepository.findById(new ServiceId(input.serviceId()))
                .orElseThrow(() -> new IllegalArgumentException("Service not found with id: " + input.serviceId()));
        if (!serviceRepository.existsById(input.serviceId())) {
            throw new IllegalArgumentException("Service not found with id: " + input.serviceId());
        }



    }
}
