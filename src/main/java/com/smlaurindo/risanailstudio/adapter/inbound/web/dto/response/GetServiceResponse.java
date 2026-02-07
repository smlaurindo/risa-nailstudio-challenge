package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response;

import com.smlaurindo.risanailstudio.application.usecase.GetService;

public record GetServiceResponse(
        String id,
        String name,
        Integer durationMinutes,
        Integer priceCents,
        String icon
) {
    public static GetServiceResponse from(GetService.GetServiceOutput output) {
        return new GetServiceResponse(
                output.id(),
                output.name(),
                output.durationMinutes(),
                output.priceCents(),
                output.icon()
        );
    }
}
