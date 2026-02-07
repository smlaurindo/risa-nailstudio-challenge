package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.application.domain.Credentials;
import com.smlaurindo.risanailstudio.application.domain.Customer;
import com.smlaurindo.risanailstudio.application.domain.Role;
import com.smlaurindo.risanailstudio.port.outbound.persistence.CredentialsRepository;
import com.smlaurindo.risanailstudio.port.outbound.persistence.CustomerRepository;
import com.smlaurindo.risanailstudio.port.outbound.security.PasswordHasher;
import com.smlaurindo.risanailstudio.shared.exception.ConflictException;
import com.smlaurindo.risanailstudio.shared.exception.ErrorCode;

public class SignUpUseCase implements SignUp {

    private final CredentialsRepository credentialsRepository;
    private final CustomerRepository customerRepository;
    private final PasswordHasher passwordHasher;

    public SignUpUseCase(
            CredentialsRepository credentialsRepository,
            CustomerRepository customerRepository,
            PasswordHasher passwordHasher
    ) {
        this.credentialsRepository = credentialsRepository;
        this.customerRepository = customerRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public void signUp(SignUpInput input) {
        if (credentialsRepository.existsByEmail(input.email())) {
            throw new ConflictException(ErrorCode.USER_ALREADY_EXISTS, "email");
        }

        String passwordHash = passwordHasher.hash(input.password());

        Credentials credentials = new Credentials(
                input.email(),
                passwordHash,
                Role.CUSTOMER
        );

        credentialsRepository.save(credentials);

        Customer customer = new Customer(credentials.getId());

        customerRepository.save(customer);
    }
}
