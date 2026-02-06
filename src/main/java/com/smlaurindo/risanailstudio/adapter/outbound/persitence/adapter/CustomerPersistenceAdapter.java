package com.smlaurindo.risanailstudio.adapter.outbound.persitence.adapter;

import com.smlaurindo.risanailstudio.adapter.outbound.persitence.entity.UserJpaEntity;
import com.smlaurindo.risanailstudio.adapter.outbound.persitence.repository.UserJpaRepository;
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

    private final UserJpaRepository userJpaRepository;

    @Override
    public Customer save(Customer customer) {
        log.debug("Saving customer: {}", customer.getEmail());

        final var entity = UserJpaEntity.fromDomain(customer);

        final var savedEntity = userJpaRepository.save(entity);

        return savedEntity.toCustomerDomain();
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        log.debug("Finding customer by email: {}", email);

        return userJpaRepository.findByEmail(email)
            .map(UserJpaEntity::toCustomerDomain);
    }

    @Override
    public Optional<Customer> findById(String id) {
        log.debug("Finding customer by id: {}", id);

        return userJpaRepository.findById(id)
            .map(UserJpaEntity::toCustomerDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        log.debug("Checking if email exists: {}", email);

        return userJpaRepository.existsByEmail(email);
    }
}
