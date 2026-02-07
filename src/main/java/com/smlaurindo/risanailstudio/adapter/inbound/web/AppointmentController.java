package com.smlaurindo.risanailstudio.adapter.inbound.web;

import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request.ScheduleAppointmentRequest;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.ScheduleAppointmentResponse;
import com.smlaurindo.risanailstudio.application.usecase.ScheduleAppointment;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
}
