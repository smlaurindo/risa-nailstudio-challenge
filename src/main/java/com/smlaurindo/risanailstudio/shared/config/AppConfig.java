package com.smlaurindo.risanailstudio.shared.config;

import com.smlaurindo.risanailstudio.application.usecase.*;

import com.smlaurindo.risanailstudio.port.outbound.persistence.*;
import com.smlaurindo.risanailstudio.port.outbound.security.PasswordHasherPort;
import com.smlaurindo.risanailstudio.port.outbound.security.TokenGeneratorPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public GetAvailableServices getAvailableServicesUseCase(
            ServiceRepositoryPort serviceRepositoryPort
    ) {
        return new GetAvailableServicesUseCase(serviceRepositoryPort);
    }

    @Bean
    public GetService getServiceUseCase(
            ServiceRepositoryPort serviceRepositoryPort
    ) {
        return new GetServiceUseCase(serviceRepositoryPort);
    }

    @Bean
    public CreateService createServiceUseCase(
            AdminRepositoryPort adminRepositoryPort,
            ServiceRepositoryPort serviceRepositoryPort
    ) {
        return new CreateServiceUseCase(adminRepositoryPort, serviceRepositoryPort);
    }

    @Bean
    public SignUp signUpUseCase(
            CredentialsRepositoryPort credentialsRepositoryPort,
            CustomerRepositoryPort customerRepositoryPort,
            PasswordHasherPort passwordHasherPort
    ) {
        return new SignUpUseCase(credentialsRepositoryPort, customerRepositoryPort, passwordHasherPort);
    }

    @Bean
    public SignIn signInUseCase(
            TokenGeneratorPort tokenGeneratorPort,
            CredentialsRepositoryPort credentialsRepositoryPort,
            PasswordHasherPort passwordHasherPort
    ) {
        return new SignInUseCase(tokenGeneratorPort, credentialsRepositoryPort, passwordHasherPort);
    }

    @Bean
    public SignOut signOutUseCase(RefreshTokenRepositoryPort refreshTokenRepositoryPort) {
        return new SignOutUseCase(refreshTokenRepositoryPort);
    }

    @Bean
    public RefreshAccessToken refreshAccessTokenUseCase(
            RefreshTokenRepositoryPort refreshTokenRepositoryPort,
            CredentialsRepositoryPort credentialsRepositoryPort,
            TokenGeneratorPort tokenGeneratorPort
    ) {
        return new RefreshAccessTokenUseCase(refreshTokenRepositoryPort, credentialsRepositoryPort, tokenGeneratorPort);
    }

    @Bean
    public ScheduleAppointment scheduleAppointmentUseCase(
            CustomerRepositoryPort customerRepositoryPort,
            ServiceRepositoryPort serviceRepositoryPort,
            AppointmentRepositoryPort appointmentRepositoryPort
    ) {
        return new ScheduleAppointmentUseCase(
                customerRepositoryPort,
                serviceRepositoryPort,
                appointmentRepositoryPort
        );
    }

    @Bean
    public ConfirmAppointment confirmAppointmentUseCase(
            AdminRepositoryPort adminRepositoryPort,
            AppointmentRepositoryPort appointmentRepositoryPort
    ) {
        return new ConfirmAppointmentUseCase(adminRepositoryPort, appointmentRepositoryPort);
    }

    @Bean
    public CancelAppointment cancelAppointmentUseCase(
            AdminRepositoryPort adminRepositoryPort,
            AppointmentRepositoryPort appointmentRepositoryPort
    ) {
        return new CancelAppointmentUseCase(adminRepositoryPort, appointmentRepositoryPort);
    }

    @Bean
    public ListAppointments listAppointmentsUseCase(
            AdminRepositoryPort adminRepositoryPort,
            AppointmentRepositoryPort appointmentRepositoryPort
    ) {
        return new ListAppointmentsUseCase(adminRepositoryPort, appointmentRepositoryPort);
    }

    @Bean
    public GetAppointment getAppointmentUseCase(
            AdminRepositoryPort adminRepositoryPort,
            AppointmentRepositoryPort appointmentRepositoryPort
    ) {
        return new GetAppointmentUseCase(adminRepositoryPort, appointmentRepositoryPort);
    }
}
