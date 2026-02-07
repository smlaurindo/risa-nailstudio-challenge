package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response;

import com.smlaurindo.risanailstudio.application.usecase.CreateService;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Created service response")
public record CreateServiceResponse(
        @Schema(description = "Service Id", example = "123e4567-e89b-12d3-a456-426614174000", type = "string", format = "uuid")
        String id,
        
        @Schema(description = "Service name", example = "Manicure")
        String name,
        
        @Schema(description = "Duration in minutes", example = "60", type = "integer")
        Integer durationMinutes,
        
        @Schema(description = "Price in cents", example = "5000", type = "integer")
        Integer priceCents,
        
        @Schema(description = "Service icon", example = "NAIL")
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
