package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.port.outbound.persistence.ServiceRepositoryPort;
import com.smlaurindo.risanailstudio.application.exception.ErrorCode;
import com.smlaurindo.risanailstudio.application.exception.NotFoundException;

public class GetServiceUseCase implements GetService {

    private final ServiceRepositoryPort serviceRepositoryPort;

    public GetServiceUseCase(ServiceRepositoryPort serviceRepositoryPort) {
        this.serviceRepositoryPort = serviceRepositoryPort;
    }

    @Override
    public GetServiceOutput getService(String serviceId) {
        final var service = serviceRepositoryPort.findById(serviceId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.SERVICE_NOT_FOUND));

        return new GetServiceOutput(
                service.getId(),
                service.getName(),
                service.getDurationMinutes(),
                service.getPriceCents(),
                service.getIcon()
        );
    }
}
