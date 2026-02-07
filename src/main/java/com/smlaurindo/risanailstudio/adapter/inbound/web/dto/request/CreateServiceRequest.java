package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request;

import com.smlaurindo.risanailstudio.application.usecase.CreateService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateServiceRequest(
        @NotBlank()
        @Size(min = 2, max = 50)
        String name,

        @NotNull()
        @Min(value = 1)
        Integer durationMinutes,

        @NotNull()
        @Min(value = 0)
        Integer priceCents,

        @NotBlank()
        String icon
) {
    public CreateService.CreateServiceInput toInput(String credentialsId) {
        return new CreateService.CreateServiceInput(
                name,
                durationMinutes,
                priceCents,
                icon,
                credentialsId
        );
    }
}
