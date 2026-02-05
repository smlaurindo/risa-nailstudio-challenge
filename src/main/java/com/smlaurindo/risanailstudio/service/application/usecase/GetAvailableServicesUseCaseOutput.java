package com.smlaurindo.risanailstudio.service.application.usecase;

public record GetAvailableServicesUseCaseOutput(
        String id,
        String name,
        String durationMinutes,
        Integer priceCents,
        String icon
) {}
