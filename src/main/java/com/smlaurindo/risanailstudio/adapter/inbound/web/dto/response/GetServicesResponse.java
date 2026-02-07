package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response;

import com.smlaurindo.risanailstudio.application.usecase.GetAvailableServices;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Service information")
public record GetServicesResponse(
        @Schema(description = "Service ID", example = "123e4567-e89b-12d3-a456-426614174000")
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
    public static List<GetServicesResponse> from(List<GetAvailableServices.GetAvailableServicesOutput> output) {
        return output
                .stream()
                .map((service) -> new GetServicesResponse(
                    service.id(),
                    service.name(),
                    service.durationMinutes(),
                    service.priceCents(),
                    service.icon()
                )).toList();
    }
}
