package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response;

import com.smlaurindo.risanailstudio.application.usecase.GetService;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Service details")
public record GetServiceResponse(
        @Schema(description = "Service Id", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,
        
        @Schema(description = "Service name", example = "Manicure")
        String name,
        
        @Schema(description = "Duration in minutes", example = "60")
        Integer durationMinutes,
        
        @Schema(description = "Price in cents", example = "5000")
        Integer priceCents,
        
        @Schema(description = "Service icon", example = "NAIL")
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
