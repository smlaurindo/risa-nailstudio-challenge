package com.smlaurindo.risanailstudio.adapter.inbound.web;

import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request.ScheduleAppointmentRequest;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.ScheduleAppointmentResponse;
import com.smlaurindo.risanailstudio.application.usecase.ScheduleAppointment;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ScheduleAppointmentResponse> scheduleAppointment(
            @Valid @RequestBody ScheduleAppointmentRequest request
    ) {
        log.info("Consumer {} scheduling a appointment", request.customerId());

        final var output = scheduleAppointment.scheduleAppointment(request.toInput());

        log.info("Consumer {} scheduled a appointment {}", request.customerId(), output.appointmentId());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(output.appointmentId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(ScheduleAppointmentResponse.fromOutput(output));
    }
}
