package com.smlaurindo.risanailstudio.adapter.inbound.web;

import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request.ScheduleAppointmentRequest;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.ConfirmAppointmentResponse;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.ScheduleAppointmentResponse;
import com.smlaurindo.risanailstudio.application.usecase.ConfirmAppointment;
import com.smlaurindo.risanailstudio.application.usecase.ScheduleAppointment;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AppointmentController {

    private final ScheduleAppointment scheduleAppointment;
    private final ConfirmAppointment confirmAppointment;

    @PostMapping(value = "/appointments", version = "1")
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

    @PatchMapping(value = "/appointments/{appointmentId}/confirm", version = "1")
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
}
