package com.smlaurindo.risanailstudio.port.outbound.persistence;

import com.smlaurindo.risanailstudio.application.domain.Customer;

import java.util.Optional;

public interface CustomerRepository {
    Customer save(Customer customer);
    Optional<Customer> findById(String id);
}
