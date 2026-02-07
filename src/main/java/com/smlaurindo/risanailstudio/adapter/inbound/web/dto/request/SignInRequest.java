package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request;

import com.smlaurindo.risanailstudio.application.usecase.SignIn;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignInRequest(
        @NotBlank()
        @Email()
        @Size(max = 255)
        String email,

        @NotBlank()
        @Size(min = 8, max = 100)
        String password
) {
    public SignInRequest {
        email = email == null ? null : email.trim().toLowerCase();
    }

    public SignIn.SignInInput toInput() {
        return new SignIn.SignInInput(email, password);
    }
}
