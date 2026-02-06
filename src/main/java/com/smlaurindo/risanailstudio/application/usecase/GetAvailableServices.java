package com.smlaurindo.risanailstudio.application.usecase;

import java.util.List;

public interface GetAvailableServices {
    record GetAvailableServicesUseCaseOutput(
            String id,
            String name,
            int durationMinutes,
            int priceCents,
            String icon
    ) {}

    List<GetAvailableServicesUseCaseOutput> getAvailableServices();
}
