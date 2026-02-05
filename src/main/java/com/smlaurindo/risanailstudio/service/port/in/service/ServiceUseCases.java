package com.smlaurindo.risanailstudio.service.port.in.service;

import com.smlaurindo.risanailstudio.service.application.usecase.GetAvailableServicesUseCaseOutput;

import java.util.List;

public interface ServiceUseCases {
    List<GetAvailableServicesUseCaseOutput> getAvailableServices();
}
