package com.smlaurindo.risanailstudio.port.outbound.persistence;

import com.smlaurindo.risanailstudio.application.domain.Service;

import java.util.List;

public interface ServiceRepository {
    List<Service> findAllAvailableServices();
}
