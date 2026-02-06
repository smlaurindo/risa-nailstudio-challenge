package com.smlaurindo.risanailstudio.shared.config;

import com.smlaurindo.risanailstudio.application.usecase.GetAvailableServices;
import com.smlaurindo.risanailstudio.application.usecase.GetAvailableServicesUseCase;

import com.smlaurindo.risanailstudio.application.usecase.SignUp;
import com.smlaurindo.risanailstudio.application.usecase.SignUpUseCase;
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

}
