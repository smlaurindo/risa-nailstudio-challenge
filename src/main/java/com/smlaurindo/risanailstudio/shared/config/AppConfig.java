package com.smlaurindo.risanailstudio.shared.config;

import com.smlaurindo.risanailstudio.application.usecase.*;

import com.smlaurindo.risanailstudio.port.outbound.persistence.*;
import com.smlaurindo.risanailstudio.port.outbound.security.PasswordHasher;
import com.smlaurindo.risanailstudio.port.outbound.security.TokenGenerator;
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
    public GetService getServiceUseCase(
            ServiceRepository serviceRepository
    ) {
        return new GetServiceUseCase(serviceRepository);
    }

    @Bean
    public CreateService createServiceUseCase(
            AdminRepository adminRepository,
            ServiceRepository serviceRepository
    ) {
        return new CreateServiceUseCase(adminRepository, serviceRepository);
    }

    @Bean
    public SignUp signUpUseCase(
            CredentialsRepository credentialsRepository,
            CustomerRepository customerRepository,
            PasswordHasher passwordHasher
    ) {
        return new SignUpUseCase(credentialsRepository, customerRepository, passwordHasher);
    }

    @Bean
    public SignIn signInUseCase(
            TokenGenerator tokenGenerator,
            CredentialsRepository credentialsRepository,
            PasswordHasher passwordHasher
    ) {
        return new SignInUseCase(tokenGenerator, credentialsRepository, passwordHasher);
    }

    @Bean
    public SignOut signOutUseCase(RefreshTokenRepository refreshTokenRepository) {
        return new SignOutUseCase(refreshTokenRepository);
    }

    @Bean
    public RefreshAccessToken refreshAccessTokenUseCase(
            RefreshTokenRepository refreshTokenRepository,
            CredentialsRepository credentialsRepository,
            TokenGenerator tokenGenerator
    ) {
        return new RefreshAccessTokenUseCase(refreshTokenRepository, credentialsRepository, tokenGenerator);
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

    @Bean
    public ConfirmAppointment confirmAppointmentUseCase(
            AdminRepository adminRepository,
            AppointmentRepository appointmentRepository
    ) {
        return new ConfirmAppointmentUseCase(adminRepository, appointmentRepository);
    }

    @Bean
    public CancelAppointment cancelAppointmentUseCase(
            AdminRepository adminRepository,
            AppointmentRepository appointmentRepository
    ) {
        return new CancelAppointmentUseCase(adminRepository, appointmentRepository);
    }

    @Bean
    public ListAppointments listAppointmentsUseCase(
            AdminRepository adminRepository,
            AppointmentRepository appointmentRepository
    ) {
        return new ListAppointmentsUseCase(adminRepository, appointmentRepository);
    }

    @Bean
    public GetAppointment getAppointmentUseCase(
            AdminRepository adminRepository,
            AppointmentRepository appointmentRepository
    ) {
        return new GetAppointmentUseCase(adminRepository, appointmentRepository);
    }
}
