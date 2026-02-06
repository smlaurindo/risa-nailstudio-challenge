package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.port.outbound.persistence.ServiceRepository;

import java.util.List;

public class GetAvailableServicesUseCase implements GetAvailableServices {

    private final ServiceRepository serviceRepository;

    public GetAvailableServicesUseCase(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<GetAvailableServicesOutput> getAvailableServices() {
        final var availableServices = serviceRepository.findAllAvailableServices();

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
