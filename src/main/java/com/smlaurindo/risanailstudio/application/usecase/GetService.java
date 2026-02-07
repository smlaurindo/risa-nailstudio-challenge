package com.smlaurindo.risanailstudio.application.usecase;

public interface GetService {
    record GetServiceOutput(
            String id,
            String name,
            int durationMinutes,
            int priceCents,
            String icon
    ) {}

    GetServiceOutput getService(String serviceId);
}
