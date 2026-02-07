package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request;

import com.smlaurindo.risanailstudio.application.usecase.CreateService;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Data for creating a new service")
public record CreateServiceRequest(
        @Schema(description = "Service name", example = "Manicure", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank()
        @Size(min = 2, max = 50)
        String name,

        @Schema(description = "Service duration in minutes", example = "60", requiredMode = Schema.RequiredMode.REQUIRED, type = "integer")
        @NotNull()
        @Min(value = 1)
        Integer durationMinutes,

        @Schema(description = "Service price in cents", example = "5000", requiredMode = Schema.RequiredMode.REQUIRED, type = "integer")
        @NotNull()
        @Min(value = 0)
        Integer priceCents,

        @Schema(description = "Service icon", example = "NAIL", requiredMode = Schema.RequiredMode.REQUIRED)
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
