package com.smlaurindo.risanailstudio.application.usecase;

public interface SignOut {
    record SignOutInput(
            String refreshToken
    ) {}

    void signOut(SignOutInput input);
}
