package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.port.persistence.ServiceRepository;

import java.util.List;

public class GetAvailableServicesUseCase implements GetAvailableServices {

    private final ServiceRepository serviceRepository;

    public GetAvailableServicesUseCase(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<GetAvailableServicesUseCaseOutput> getAvailableServices() {
        final var availableServices = serviceRepository.findAllAvailableServices();

        return availableServices.stream()
                .map(availableService -> new GetAvailableServicesUseCaseOutput(
                        availableService.getId(),
                        availableService.getName(),
                        availableService.getDurationMinutes(),
                        availableService.getPriceCents(),
                        availableService.getIcon()
                )).toList();
    }
}
