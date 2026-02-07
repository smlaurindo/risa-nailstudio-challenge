package com.smlaurindo.risanailstudio.adapter.inbound.web;

import com.smlaurindo.risanailstudio.application.usecase.CreateService;
import com.smlaurindo.risanailstudio.application.usecase.GetAvailableServices;
import com.smlaurindo.risanailstudio.application.usecase.GetService;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request.CreateServiceRequest;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.CreateServiceResponse;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.GetServicesResponse;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.GetServiceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ServiceController {

    private final GetAvailableServices getAvailableServices;
    private final GetService getService;
    private final CreateService createService;

    @GetMapping(value = "/services", version = "1")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<List<GetServicesResponse>> getAvailableServices() {
        log.info("Fetching all available services");

        final var output = getAvailableServices.getAvailableServices();

        log.info("Found {} available services", output.size());

        return ResponseEntity.ok(GetServicesResponse.from(output));
    }

    @GetMapping(value = "/services/{serviceId}", version = "1")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<GetServiceResponse> getService(@PathVariable String serviceId) {
        log.info("Fetching service: {}", serviceId);

        final var output = getService.getService(serviceId);

        log.info("Service {} found", serviceId);

        return ResponseEntity.ok(GetServiceResponse.from(output));
    }

    @PostMapping(value = "/services", version = "1")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateServiceResponse> createService(
            @AuthenticationPrincipal Jwt jwt,
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
