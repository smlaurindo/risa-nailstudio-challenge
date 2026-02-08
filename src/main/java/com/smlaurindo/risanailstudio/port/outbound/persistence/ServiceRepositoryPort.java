package com.smlaurindo.risanailstudio.port.outbound.persistence;

import com.smlaurindo.risanailstudio.application.domain.Service;

import java.util.List;
import java.util.Optional;

public interface ServiceRepositoryPort {
    List<Service> findAllAvailableServices();
    Optional<Service> findById(String serviceId);
    Service save(Service service);
}
