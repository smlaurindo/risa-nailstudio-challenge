package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.port.outbound.persistence.ServiceRepository;
import com.smlaurindo.risanailstudio.shared.exception.ErrorCode;
import com.smlaurindo.risanailstudio.shared.exception.NotFoundException;

public class GetServiceUseCase implements GetService {

    private final ServiceRepository serviceRepository;

    public GetServiceUseCase(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public GetServiceOutput getService(String serviceId) {
        final var service = serviceRepository.findById(serviceId)
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
