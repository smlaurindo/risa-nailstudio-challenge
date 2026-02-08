package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.port.outbound.persistence.ServiceRepositoryPort;

import java.util.List;

public class GetAvailableServicesUseCase implements GetAvailableServices {

    private final ServiceRepositoryPort serviceRepositoryPort;

    public GetAvailableServicesUseCase(ServiceRepositoryPort serviceRepositoryPort) {
        this.serviceRepositoryPort = serviceRepositoryPort;
    }

    public List<GetAvailableServicesOutput> getAvailableServices() {
        final var availableServices = serviceRepositoryPort.findAllAvailableServices();

        return availableServices.stream()
                .map(availableService -> new GetAvailableServicesOutput(
                        availableService.getId(),
                        availableService.getName(),
                        availableService.getDurationMinutes(),
                        availableService.getPriceCents(),
                        availableService.getIcon()
                )).toList();
    }
}
