package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.application.domain.Service;
import com.smlaurindo.risanailstudio.port.outbound.persistence.AdminRepository;
import com.smlaurindo.risanailstudio.port.outbound.persistence.ServiceRepository;
import com.smlaurindo.risanailstudio.application.exception.AuthorizationException;
import com.smlaurindo.risanailstudio.application.exception.ErrorCode;

public class CreateServiceUseCase implements CreateService {

    private final AdminRepository adminRepository;
    private final ServiceRepository serviceRepository;

    public CreateServiceUseCase(AdminRepository adminRepository, ServiceRepository serviceRepository) {
        this.adminRepository = adminRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public CreateServiceOutput createService(CreateServiceInput input) {
        adminRepository.findByCredentialsId(input.credentialsId())
                .orElseThrow(() -> new AuthorizationException(ErrorCode.INSUFFICIENT_PRIVILEGES));

        Service service = new Service(
                input.name(),
                input.durationMinutes(),
                input.priceCents(),
                input.icon()
        );

        Service savedService = serviceRepository.save(service);

        return new CreateServiceOutput(
                savedService.getId(),
                savedService.getName(),
                savedService.getDurationMinutes(),
                savedService.getPriceCents(),
                savedService.getIcon()
        );
    }
}
