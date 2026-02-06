package com.smlaurindo.risanailstudio.application.usecase;

public interface SignUp {
    record SignUpInput(
            String email,
            String password
    ) {}
    void signUp(SignUpInput input);
}
