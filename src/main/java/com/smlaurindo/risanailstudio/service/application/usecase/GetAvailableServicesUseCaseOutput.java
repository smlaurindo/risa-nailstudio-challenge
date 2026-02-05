package com.smlaurindo.risanailstudio.service.application.usecase;

public record GetAvailableServicesUseCaseOutput(
        String id,
        String name,
        int durationMinutes,
        int priceCents,
        String icon
) {}
