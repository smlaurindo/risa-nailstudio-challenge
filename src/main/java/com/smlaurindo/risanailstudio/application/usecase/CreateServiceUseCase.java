package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.application.domain.Service;
import com.smlaurindo.risanailstudio.port.outbound.persistence.AdminRepositoryPort;
import com.smlaurindo.risanailstudio.port.outbound.persistence.ServiceRepositoryPort;
import com.smlaurindo.risanailstudio.application.exception.AuthorizationException;
import com.smlaurindo.risanailstudio.application.exception.ErrorCode;

public class CreateServiceUseCase implements CreateService {

    private final AdminRepositoryPort adminRepositoryPort;
    private final ServiceRepositoryPort serviceRepositoryPort;

    public CreateServiceUseCase(AdminRepositoryPort adminRepositoryPort, ServiceRepositoryPort serviceRepositoryPort) {
        this.adminRepositoryPort = adminRepositoryPort;
        this.serviceRepositoryPort = serviceRepositoryPort;
    }

    @Override
    public CreateServiceOutput createService(CreateServiceInput input) {
        adminRepositoryPort.findByCredentialsId(input.credentialsId())
                .orElseThrow(() -> new AuthorizationException(ErrorCode.INSUFFICIENT_PRIVILEGES));

        Service service = new Service(
                input.name(),
                input.durationMinutes(),
                input.priceCents(),
                input.icon()
        );

        Service savedService = serviceRepositoryPort.save(service);

        return new CreateServiceOutput(
                savedService.getId(),
                savedService.getName(),
                savedService.getDurationMinutes(),
                savedService.getPriceCents(),
                savedService.getIcon()
        );
    }
}
