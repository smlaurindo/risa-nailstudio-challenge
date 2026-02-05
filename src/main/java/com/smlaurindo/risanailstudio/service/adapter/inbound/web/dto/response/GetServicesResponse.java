package com.smlaurindo.risanailstudio.service.adapter.inbound.web.dto.response;

import com.smlaurindo.risanailstudio.service.application.usecase.GetAvailableServicesUseCaseOutput;

import java.util.List;

public record GetServicesResponse(
        String id,
        String name,
        String durationMinutes,
        Integer priceCents,
        String icon
) {
    public static List<GetServicesResponse> from(List<GetAvailableServicesUseCaseOutput> output) {
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
