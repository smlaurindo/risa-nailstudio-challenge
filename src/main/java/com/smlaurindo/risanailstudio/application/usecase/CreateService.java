package com.smlaurindo.risanailstudio.application.usecase;

public interface CreateService {
    record CreateServiceInput(
            String name,
            int durationMinutes,
            int priceCents,
            String icon,
            String credentialsId
    ) {}

    record CreateServiceOutput(
            String id,
            String name,
            int durationMinutes,
            int priceCents,
            String icon
    ) {}

    CreateServiceOutput createService(CreateServiceInput input);
}
