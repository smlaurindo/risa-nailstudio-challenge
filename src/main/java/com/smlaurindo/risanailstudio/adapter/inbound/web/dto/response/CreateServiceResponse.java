package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response;

import com.smlaurindo.risanailstudio.application.usecase.CreateService;

public record CreateServiceResponse(
        String id,
        String name,
        Integer durationMinutes,
        Integer priceCents,
        String icon
) {
    public static CreateServiceResponse from(CreateService.CreateServiceOutput output) {
        return new CreateServiceResponse(
                output.id(),
                output.name(),
                output.durationMinutes(),
                output.priceCents(),
                output.icon()
        );
    }
}
