package com.smlaurindo.risanailstudio.service.adapter.inbound.service;

import com.smlaurindo.risanailstudio.service.application.usecase.GetAvailableServicesUseCase;
import com.smlaurindo.risanailstudio.service.application.usecase.GetAvailableServicesUseCaseOutput;
import com.smlaurindo.risanailstudio.service.port.in.service.ServiceUseCases;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceService implements ServiceUseCases {

    private final GetAvailableServicesUseCase getAvailableServicesUseCase;

    @Override
    public List<GetAvailableServicesUseCaseOutput> getAvailableServices() {
        return getAvailableServicesUseCase.getAvailableServices();
    }
}
