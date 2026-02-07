package com.smlaurindo.risanailstudio.adapter.inbound.web;

import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request.AppointmentStatusFilter;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request.ScheduleAppointmentRequest;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.ApiErrorResponse;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.CancelAppointmentResponse;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.ConfirmAppointmentResponse;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.GetAppointmentResponse;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.ListAppointmentsResponse;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.ScheduleAppointmentResponse;
import com.smlaurindo.risanailstudio.application.domain.AppointmentStatus;
import com.smlaurindo.risanailstudio.application.usecase.CancelAppointment;
import com.smlaurindo.risanailstudio.application.usecase.ConfirmAppointment;
import com.smlaurindo.risanailstudio.application.usecase.GetAppointment;
import com.smlaurindo.risanailstudio.application.usecase.ListAppointments;
import com.smlaurindo.risanailstudio.application.usecase.ScheduleAppointment;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "Appointments", description = "Endpoints for managing service appointments")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/appointments")
public class AppointmentController {

    private final ScheduleAppointment scheduleAppointment;
    private final ConfirmAppointment confirmAppointment;
    private final CancelAppointment cancelAppointment;
    private final GetAppointment getAppointment;
    private final ListAppointments listAppointments;

    @Operation(
            summary = "Schedule an appointment",
            description = "Creates a new appointment for a service. Customer must provide their name if they do not have one. Date/time must be in the future.",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "accessTokenCookieAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Appointment successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ScheduleAppointmentResponse.class)
                    ),
                    headers = {
                        @Header(
                                name = "Location",
                                description = "URI of the newly scheduled appointment",
                                schema = @Schema(type = "string", format = "uri"),
                                example = "/v1/appointments/123e4567-e89b-12d3-a456-426614174000"
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
                                            "code": "REQUIRED",
                                            "field": "serviceId"
                                          },
                                          {
                                            "code": "INVALID_DATE",
                                            "field": "scheduledAt"
                                          }
                                        ],
                                        "requestId": "req_abc123"
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
                                            "code": "INVALID_CREDENTIALS",
                                            "requestId": "req_l5d2o16e4yxi"
                                        },
                                        "statusCode": 401
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Insufficient permissions (customers only)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                                            {
                                                "error": {
                                                    "type": "authorization_error",
                                                    "code": "INSUFFICIENT_PERMISSIONS",
                                                    "requestId": "req_l5d2o16e4yxi"
                                                },
                                                "statusCode": 403
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
                                        "requestId": "req_abc123"
                                      },
                                      "statusCode": 404
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Time slot unavailable/invalid or customer name required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "error": {
                                        "type": "business_rule",
                                        "code": "APPOINTMENT_SLOT_UNAVAILABLE",
                                        "field": "scheduledAt",
                                        "requestId": "req_abc123"
                                      },
                                      "statusCode": 422
                                    }
                                    """)
                    )
            )
    })
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ScheduleAppointmentResponse> scheduleAppointment(
            @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ScheduleAppointmentRequest request
    ) {
        final String userId = jwt.getSubject();

        log.info("Customer with credentials id {} scheduling an appointment", userId);

        final var output = scheduleAppointment.scheduleAppointment(request.toInput(userId));

        log.info("Customer with credentials id {} scheduled an appointment {} successfully", userId, output.appointmentId());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(output.appointmentId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(ScheduleAppointmentResponse.fromOutput(output));
    }

    @Operation(
            summary = "List appointments",
            description = "Lists all appointments within a specific period, with optional filters by status and search. Requires administrator permissions.",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "accessTokenCookieAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointments list successfully returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ListAppointmentsResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "error": {
                                        "type": "validation_error",
                                        "errors": [
                                          {
                                            "code": "REQUIRED",
                                            "field": "startDate"
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
                                            "code": "INVALID_CREDENTIALS",
                                            "requestId": "req_l5d2o16e4yxi"
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
                                                    "code": "INSUFFICIENT_PERMISSIONS",
                                                    "requestId": "req_l5d2o16e4yxi"
                                                },
                                                "statusCode": 403
                                            }
                                            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Invalid or too long date range",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "error": {
                                            "type": "business_rule",
                                            "code": "DATE_RANGE_TOO_LONG",
                                            "requestId": "req_7hd70uiydd0a"
                                        },
                                        "statusCode": 422
                                    }
                                    """)
                    )
            )
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ListAppointmentsResponse>> listAppointments(
            @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "Start date (format: YYYY-MM-DD)", required = true, example = "2026-02-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (format: YYYY-MM-DD)", required = true, example = "2026-02-28")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Filter by appointment status", example = "ALL")
            @RequestParam(defaultValue = "ALL") AppointmentStatusFilter status,
            @Parameter(description = "Search by customer name or service")
            @RequestParam(required = false) String searchQuery
    ) {
        final String userId = jwt.getSubject();

        final AppointmentStatus appointmentStatus = status.toAppointmentStatus();

        log.info("Admin with credentials id {} listing appointments from {} to {} with status {} and search query '{}'",
                userId,
                startDate,
                endDate,
                appointmentStatus,
                searchQuery
        );

        final var input = new ListAppointments.ListAppointmentsInput(
                startDate,
                endDate,
                appointmentStatus,
                searchQuery,
                userId
        );

        final var output = listAppointments.listAppointments(input);

        log.info("Admin with credentials id {} retrieved {} appointments", userId, output.size());

        return ResponseEntity
                .ok()
                .body(ListAppointmentsResponse.from(output));
    }

    @Operation(
            summary = "Get appointment details",
            description = "Returns complete details of a specific appointment. Requires administrator permissions.",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "accessTokenCookieAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointment found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetAppointmentResponse.class)
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
                                            "code": "INVALID_CREDENTIALS",
                                            "requestId": "req_l5d2o16e4yxi"
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
                                            "code": "INSUFFICIENT_PERMISSIONS",
                                            "requestId": "req_l5d2o16e4yxi"
                                        },
                                        "statusCode": 403
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appointment not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "error": {
                                        "type": "not_found",
                                        "code": "APPOINTMENT_NOT_FOUND",
                                        "field": "appointmentId",
                                        "requestId": "abc123"
                                      },
                                      "statusCode": 404
                                    }
                                    """)
                    )
            )
    })
    @GetMapping(path = "/{appointmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GetAppointmentResponse> getAppointment(
            @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "Appointment ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String appointmentId
    ) {
        final String userId = jwt.getSubject();

        log.info("Admin with credentials id {} getting appointment {}", userId, appointmentId);

        final var output = getAppointment.getAppointment(
                new GetAppointment.GetAppointmentInput(appointmentId, userId)
        );

        log.info("Admin with credentials id {} retrieved appointment {} successfully", userId, appointmentId);

        return ResponseEntity
                .ok()
                .body(GetAppointmentResponse.fromOutput(output));
    }

    @Operation(
            summary = "Confirm appointment",
            description = "Confirms a pending appointment. Requires administrator permissions.",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "accessTokenCookieAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointment successfully confirmed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConfirmAppointmentResponse.class)
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
                                                "code": "INVALID_CREDENTIALS",
                                                "requestId": "req_l5d2o16e4yxi"
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
                                                "code": "INSUFFICIENT_PERMISSIONS",
                                                "requestId": "req_l5d2o16e4yxi"
                                            },
                                            "statusCode": 403
                                        }
                                        """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appointment not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                       "error": {
                                        "type": "not_found",
                                        "code": "APPOINTMENT_NOT_FOUND",
                                        "field": "appointmentId",
                                        "requestId": "req_abc123"
                                       },
                                       "statusCode": 404
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Appointment already confirmed or cancelled",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "error": {
                                        "type": "business_rule",
                                        "code": "APPOINTMENT_ALREADY_CONFIRMED",
                                        "field": "appointmentId",
                                        "requestId": "req_abc123"
                                      },
                                      "statusCode": 422
                                    }
                                    """)
                    )
            )
    })
    @PatchMapping(path = "{appointmentId}/confirm")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConfirmAppointmentResponse> confirmAppointment(
            @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "Appointment Id", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String appointmentId
    ) {
        final String userId = jwt.getSubject();

        log.info("Admin with credentials id {} confirming appointment {}", userId, appointmentId);

        final var output = confirmAppointment.confirmAppointment(
                new ConfirmAppointment.ConfirmAppointmentInput(appointmentId, userId)
        );

        log.info("Admin with credentials id {} confirmed appointment {} successfully", userId, output.appointmentId());

        return ResponseEntity
                .ok()
                .body(ConfirmAppointmentResponse.fromOutput(output));
    }

    @Operation(
            summary = "Cancel appointment",
            description = "Cancels a pending or confirmed appointment. Requires administrator permissions.",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "accessTokenCookieAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointment successfully cancelled",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CancelAppointmentResponse.class)
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
                                                        "code": "INVALID_CREDENTIALS",
                                                        "requestId": "req_l5d2o16e4yxi"
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
                                                "code": "INSUFFICIENT_PERMISSIONS",
                                                "requestId": "req_l5d2o16e4yxi"
                                            },
                                            "statusCode": 403
                                        }
                                        """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appointment not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                                        {
                                            "error": {
                                                "type": "not_found",
                                                "code": "APPOINTMENT_NOT_FOUND",
                                                "field": "appointmentId",
                                                "requestId": "req_abc123"
                                            },
                                            "statusCode": 404
                                        }
                                        """)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Appointment already cancelled",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "error": {
                                        "type": "business_rule",
                                        "code": "APPOINTMENT_ALREADY_CANCELLED",
                                        "field": "appointmentId",
                                        "requestId": "req_abc123"
                                      },
                                      "statusCode": 422
                                    }
                                    """)
                    )
            )
    })
    @PatchMapping(path = "{appointmentId}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CancelAppointmentResponse> cancelAppointment(
            @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "Appointment ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String appointmentId
    ) {
        final String userId = jwt.getSubject();

        log.info("Admin with credentials id {} cancelling appointment {}", userId, appointmentId);

        final var output = cancelAppointment.cancelAppointment(
                new CancelAppointment.CancelAppointmentInput(appointmentId, userId)
        );

        log.info("Admin with credentials id {} cancelled appointment {} successfully", userId, output.appointmentId());

        return ResponseEntity
                .ok()
                .body(CancelAppointmentResponse.fromOutput(output));
    }
}
