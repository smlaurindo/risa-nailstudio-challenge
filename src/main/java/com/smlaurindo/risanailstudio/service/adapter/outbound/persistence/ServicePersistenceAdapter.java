package com.smlaurindo.risanailstudio.service.adapter.outbound.persistence;

import com.smlaurindo.risanailstudio.service.application.domain.Service;
import com.smlaurindo.risanailstudio.service.port.outbound.persistence.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ServicePersistenceAdapter implements ServiceRepository {

    private final ServiceJpaRepository serviceJpaRepository;

    @Override
    public List<Service> findAllAvailableServices() {
        var serviceJpaEntities = serviceJpaRepository.findAll();

        return serviceJpaEntities.stream().map(ServiceJpaEntity::toDomain).toList();
    }
}
