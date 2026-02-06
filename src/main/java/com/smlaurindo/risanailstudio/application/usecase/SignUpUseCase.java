package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.application.domain.Customer;
import com.smlaurindo.risanailstudio.port.outbound.persistence.CustomerRepository;
import com.smlaurindo.risanailstudio.port.outbound.security.PasswordHasher;
import com.smlaurindo.risanailstudio.shared.exception.ConflictException;
import com.smlaurindo.risanailstudio.shared.exception.ErrorCode;

public class SignUpUseCase implements SignUp {

    private final CustomerRepository customerRepository;
    private final PasswordHasher passwordHasher;

    public SignUpUseCase(CustomerRepository customerRepository, PasswordHasher passwordHasher) {
        this.customerRepository = customerRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public void signUp(SignUpInput input) {
        if (customerRepository.existsByEmail(input.email())) {
            throw new ConflictException(ErrorCode.USER_ALREADY_EXISTS, "email");
        }

        String passwordHash = passwordHasher.hash(input.password());

        Customer customer = new Customer(
                input.email(),
                passwordHash
        );

        customerRepository.save(customer);
    }
}
