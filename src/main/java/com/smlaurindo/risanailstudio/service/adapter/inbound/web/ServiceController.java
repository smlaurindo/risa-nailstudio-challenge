package com.smlaurindo.risanailstudio.service.adapter.inbound.web;

import com.smlaurindo.risanailstudio.service.adapter.inbound.service.ServiceService;
import com.smlaurindo.risanailstudio.service.adapter.inbound.web.dto.response.GetServicesResponse;
import com.smlaurindo.risanailstudio.service.application.usecase.GetAvailableServicesUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    @GetMapping(value = "/services", version = "1")
    public ResponseEntity<List<GetServicesResponse>> getAvailableServices() {
        final var output = serviceService.getAvailableServices();
        return ResponseEntity.ok(GetServicesResponse.from(output));
    }
}
