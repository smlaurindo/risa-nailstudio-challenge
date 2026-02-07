package com.smlaurindo.risanailstudio.adapter.inbound.web;

import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request.AppointmentStatusFilter;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request.ScheduleAppointmentRequest;
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

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ScheduleAppointmentResponse> scheduleAppointment(
            @AuthenticationPrincipal Jwt jwt,
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

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ListAppointmentsResponse>> listAppointments(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "ALL") AppointmentStatusFilter status,
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

    @GetMapping(path = "/{appointmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GetAppointmentResponse> getAppointment(
            @AuthenticationPrincipal Jwt jwt,
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

    @PatchMapping(path = "{appointmentId}/confirm")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConfirmAppointmentResponse> confirmAppointment(
            @AuthenticationPrincipal Jwt jwt,
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

    @PatchMapping(path = "{appointmentId}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CancelAppointmentResponse> cancelAppointment(
            @AuthenticationPrincipal Jwt jwt,
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
