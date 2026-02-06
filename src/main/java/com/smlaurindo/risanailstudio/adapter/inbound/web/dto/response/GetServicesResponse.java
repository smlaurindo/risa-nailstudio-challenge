package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response;

import com.smlaurindo.risanailstudio.application.usecase.GetAvailableServices;

import java.util.List;

public record GetServicesResponse(
        String id,
        String name,
        Integer durationMinutes,
        Integer priceCents,
        String icon
) {
    public static List<GetServicesResponse> from(List<GetAvailableServices.GetAvailableServicesOutput> output) {
        return output
                .stream()
                .map((service) -> new GetServicesResponse(
                    service.id(),
                    service.name(),
                    service.durationMinutes(),
                    service.priceCents(),
                    service.icon()
                )).toList();
    }
}
