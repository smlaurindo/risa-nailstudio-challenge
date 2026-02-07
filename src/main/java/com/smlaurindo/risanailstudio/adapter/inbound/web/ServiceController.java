package com.smlaurindo.risanailstudio.adapter.inbound.web;

import com.smlaurindo.risanailstudio.application.usecase.CreateService;
import com.smlaurindo.risanailstudio.application.usecase.GetAvailableServices;
import com.smlaurindo.risanailstudio.application.usecase.GetService;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request.CreateServiceRequest;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.ApiErrorResponse;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.CreateServiceResponse;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.GetServicesResponse;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.GetServiceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Services", description = "Endpoints for managing services offered by the salon")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/services")
public class ServiceController {

    private final GetAvailableServices getAvailableServices;
    private final GetService getService;
    private final CreateService createService;

    @Operation(
            summary = "List available services",
            description = "Returns a list of all active services offered by the salon. Requires authentication.",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "accessTokenCookieAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Services list successfully returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetServicesResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Not authenticated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "error": {
                                        "type": "authentication_error",
                                        "code": "INVALID_TOKEN",
                                        "requestId": "abc123"
                                      },
                                      "statusCode": 401
                                    }
                                    """)
                    )
            )
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<List<GetServicesResponse>> getAvailableServices() {
        log.info("Fetching all available services");

        final var output = getAvailableServices.getAvailableServices();

        log.info("Found {} available services", output.size());

        return ResponseEntity.ok(GetServicesResponse.from(output));
    }

    @Operation(
            summary = "Get service details",
            description = "Returns complete details of a specific service. Requires authentication.",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "accessTokenCookieAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Service found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetServiceResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Not authenticated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "error": {
                                        "type": "authentication_error",
                                        "code": "INVALID_TOKEN",
                                        "requestId": "abc123"
                                      },
                                      "statusCode": 401
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Service not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "error": {
                                        "type": "not_found",
                                        "code": "SERVICE_NOT_FOUND",
                                        "field": "serviceId",
                                        "requestId": "abc123"
                                      },
                                      "statusCode": 404
                                    }
                                    """)
                    )
            )
    })
    @GetMapping(path = "/{serviceId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<GetServiceResponse> getService(
            @Parameter(description = "Service ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String serviceId
    ) {
        log.info("Fetching service: {}", serviceId);

        final var output = getService.getService(serviceId);

        log.info("Service {} found", serviceId);

        return ResponseEntity.ok(GetServiceResponse.from(output));
    }

    @Operation(
            summary = "Create new service",
            description = "Creates a new service in the system. Requires administrator permissions.",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "accessTokenCookieAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Service successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateServiceResponse.class)
                    ),
                    headers = {
                            @Header(
                                    name = "Location",
                                    description = "URI of the newly created service",
                                    schema = @Schema(type = "string", format = "uri"),
                                    example = "/v1/services/123e4567-e89b-12d3-a456-426614174000"
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "error": {
                                        "type": "validation_error",
                                        "errors": [
                                          {
                                            "code": "TOO_SHORT",
                                            "field": "name"
                                          },
                                          {
                                            "code": "OUT_OF_RANGE",
                                            "field": "durationMinutes"
                                          }
                                        ],
                                        "requestId": "abc123"
                                      },
                                      "statusCode": 400
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Not authenticated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "error": {
                                        "type": "authentication_error",
                                        "code": "INVALID_TOKEN",
                                        "requestId": "abc123"
                                      },
                                      "statusCode": 401
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Insufficient permissions (administrators only)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "error": {
                                        "type": "authorization_error",
                                        "code": "INSUFFICIENT_PRIVILEGES",
                                        "requestId": "abc123"
                                      },
                                      "statusCode": 403
                                    }
                                    """)
                    )
            )
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateServiceResponse> createService(
            @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateServiceRequest request
    ) {
        final String userId = jwt.getSubject();

        log.info("Admin with credentials id {} creating a new service: {}", userId, request.name());

        final var output = createService.createService(request.toInput(userId));

        log.info("Admin with credentials id {} successfully created service {} with id: {}", userId, output.name(), output.id());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(output.id())
                .toUri();

        return ResponseEntity.created(location).body(CreateServiceResponse.from(output));
    }
}
