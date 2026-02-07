package com.smlaurindo.risanailstudio.adapter.outbound.persitence.adapter;

import com.smlaurindo.risanailstudio.adapter.outbound.persitence.entity.CustomerJpaEntity;
import com.smlaurindo.risanailstudio.adapter.outbound.persitence.entity.UserJpaEntity;
import com.smlaurindo.risanailstudio.adapter.outbound.persitence.repository.CustomerJpaRepository;
import com.smlaurindo.risanailstudio.application.domain.Customer;
import com.smlaurindo.risanailstudio.port.outbound.persistence.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerPersistenceAdapter implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    @Override
    public Customer save(Customer customer) {
        log.debug("Saving customer: {}", customer.getId());

        CustomerJpaEntity entity = CustomerJpaEntity.builder()
                .id(customer.getId())
                .user(new UserJpaEntity(customer.getCredentialsId()))
                .name(customer.getName())
                .photo(customer.getPhoto())
                .build();

        CustomerJpaEntity saved = customerJpaRepository.save(entity);

        return new Customer(
                saved.getId(),
                saved.getUser().getId(),
                saved.getName(),
                saved.getPhoto()
        );
    }

    @Override
    public Optional<Customer> findByCredentialsId(String credentialsId) {
        log.debug("Finding customer by credentialsId: {}", credentialsId);

        return customerJpaRepository.findByUserId(credentialsId)
                .map(entity -> new Customer(
                        entity.getId(),
                        entity.getUser().getId(),
                        entity.getName(),
                        entity.getPhoto()
                ));
    }
}

