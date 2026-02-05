package com.smlaurindo.risanailstudio.service.port.outbound.persistence;

import java.util.List;

import com.smlaurindo.risanailstudio.service.application.domain.Service;

public interface ServiceRepository {
    List<Service> findAllAvailableServices();
}
