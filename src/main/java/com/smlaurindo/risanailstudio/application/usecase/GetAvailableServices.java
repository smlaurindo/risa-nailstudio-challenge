package com.smlaurindo.risanailstudio.application.usecase;

import java.util.List;

public interface GetAvailableServices {
    record GetAvailableServicesOutput(
            String id,
            String name,
            int durationMinutes,
            int priceCents,
            String icon
    ) {}

    List<GetAvailableServicesOutput> getAvailableServices();
}
