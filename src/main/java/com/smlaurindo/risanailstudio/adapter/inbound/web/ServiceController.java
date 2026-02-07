package com.smlaurindo.risanailstudio.adapter.inbound.web;

import com.smlaurindo.risanailstudio.application.usecase.GetAvailableServices;
import com.smlaurindo.risanailstudio.application.usecase.GetService;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.GetServicesResponse;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.GetServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ServiceController {

    private final GetAvailableServices getAvailableServices;
    private final GetService getService;

    @GetMapping(value = "/services", version = "1")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<List<GetServicesResponse>> getAvailableServices() {
        final var output = getAvailableServices.getAvailableServices();
        return ResponseEntity.ok(GetServicesResponse.from(output));
    }

    @GetMapping(value = "/services/{serviceId}", version = "1")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<GetServiceResponse> getService(@PathVariable String serviceId) {
        final var output = getService.getService(serviceId);
        return ResponseEntity.ok(GetServiceResponse.from(output));
    }
}
