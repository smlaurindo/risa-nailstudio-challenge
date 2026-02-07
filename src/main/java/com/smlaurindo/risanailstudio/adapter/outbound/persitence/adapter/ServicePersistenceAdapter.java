package com.smlaurindo.risanailstudio.adapter.outbound.persitence.adapter;

import com.smlaurindo.risanailstudio.adapter.outbound.persitence.entity.ServiceJpaEntity;
import com.smlaurindo.risanailstudio.adapter.outbound.persitence.repository.ServiceJpaRepository;
import com.smlaurindo.risanailstudio.application.domain.Service;
import com.smlaurindo.risanailstudio.port.outbound.persistence.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ServicePersistenceAdapter implements ServiceRepository {

    private final ServiceJpaRepository serviceJpaRepository;

    @Override
    public List<Service> findAllAvailableServices() {
        var serviceJpaEntities = serviceJpaRepository.findAll();

        return serviceJpaEntities.stream().map(ServiceJpaEntity::toDomain).toList();
    }

    @Override
    public Optional<Service> findById(String serviceId) {
        return serviceJpaRepository.findById(serviceId).map(ServiceJpaEntity::toDomain);
    }
}
