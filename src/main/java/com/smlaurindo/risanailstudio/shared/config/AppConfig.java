package com.smlaurindo.risanailstudio.shared.config;

import com.smlaurindo.risanailstudio.application.usecase.*;

import com.smlaurindo.risanailstudio.port.outbound.persistence.AppointmentRepository;
import com.smlaurindo.risanailstudio.port.outbound.persistence.CustomerRepository;
import com.smlaurindo.risanailstudio.port.outbound.persistence.ServiceRepository;
import com.smlaurindo.risanailstudio.port.outbound.security.PasswordHasher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public GetAvailableServices getAvailableServicesUseCase(
            ServiceRepository serviceRepository
    ) {
        return new GetAvailableServicesUseCase(serviceRepository);
    }

    @Bean
    public SignUp signUpUseCase(
            CustomerRepository customerRepository,
            PasswordHasher passwordHasher
    ) {
        return new SignUpUseCase(customerRepository, passwordHasher);
    }

    @Bean
    public ScheduleAppointment scheduleAppointmentUseCase(
            CustomerRepository customerRepository,
            ServiceRepository serviceRepository,
            AppointmentRepository appointmentRepository
    ) {
        return new ScheduleAppointmentUseCase(
                customerRepository,
                serviceRepository,
                appointmentRepository
        );
    }
}
