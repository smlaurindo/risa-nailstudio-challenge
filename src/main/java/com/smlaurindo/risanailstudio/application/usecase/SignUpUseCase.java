package com.smlaurindo.risanailstudio.application.usecase;

import com.smlaurindo.risanailstudio.application.domain.Credentials;
import com.smlaurindo.risanailstudio.application.domain.Customer;
import com.smlaurindo.risanailstudio.application.domain.Role;
import com.smlaurindo.risanailstudio.port.outbound.persistence.CredentialsRepositoryPort;
import com.smlaurindo.risanailstudio.port.outbound.persistence.CustomerRepositoryPort;
import com.smlaurindo.risanailstudio.port.outbound.security.PasswordHasherPort;
import com.smlaurindo.risanailstudio.application.exception.ConflictException;
import com.smlaurindo.risanailstudio.application.exception.ErrorCode;

public class SignUpUseCase implements SignUp {

    private final CredentialsRepositoryPort credentialsRepositoryPort;
    private final CustomerRepositoryPort customerRepositoryPort;
    private final PasswordHasherPort passwordHasherPort;

    public SignUpUseCase(
            CredentialsRepositoryPort credentialsRepositoryPort,
            CustomerRepositoryPort customerRepositoryPort,
            PasswordHasherPort passwordHasherPort
    ) {
        this.credentialsRepositoryPort = credentialsRepositoryPort;
        this.customerRepositoryPort = customerRepositoryPort;
        this.passwordHasherPort = passwordHasherPort;
    }

    @Override
    public void signUp(SignUpInput input) {
        if (credentialsRepositoryPort.existsByEmail(input.email())) {
            throw new ConflictException(ErrorCode.USER_ALREADY_EXISTS, "email");
        }

        String passwordHash = passwordHasherPort.hash(input.password());

        Credentials credentials = new Credentials(
                input.email(),
                passwordHash,
                Role.CUSTOMER
        );

        credentialsRepositoryPort.save(credentials);

        Customer customer = new Customer(credentials.getId());

        customerRepositoryPort.save(customer);
    }
}
