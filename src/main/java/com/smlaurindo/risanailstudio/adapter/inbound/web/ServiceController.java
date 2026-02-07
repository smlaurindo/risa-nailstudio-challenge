package com.smlaurindo.risanailstudio.adapter.inbound.web;

import com.smlaurindo.risanailstudio.application.usecase.GetAvailableServices;
import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.response.GetServicesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ServiceController {

    private final GetAvailableServices getAvailableServices;

    @GetMapping(value = "/services", version = "1")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<List<GetServicesResponse>> getAvailableServices() {
        final var output = getAvailableServices.getAvailableServices();
        return ResponseEntity.ok(GetServicesResponse.from(output));
    }
}
