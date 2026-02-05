package com.smlaurindo.risanailstudio.shared.config;

import com.smlaurindo.risanailstudio.service.application.usecase.GetAvailableServicesUseCase;
import com.smlaurindo.risanailstudio.service.port.outbound.persistence.ServiceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public GetAvailableServicesUseCase getAvailableServicesUseCase(
            ServiceRepository serviceRepository
    ) {
        return new GetAvailableServicesUseCase(serviceRepository);
    }
}
