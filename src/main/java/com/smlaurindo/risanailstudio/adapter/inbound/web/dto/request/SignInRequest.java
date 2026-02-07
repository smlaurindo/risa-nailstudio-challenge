package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request;

import com.smlaurindo.risanailstudio.application.usecase.SignIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Credentials for authentication")
public record SignInRequest(
        @Schema(
                description = "User email",
                example = "user@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 255,
                type = "string"
        )
        @NotBlank()
        @Email()
        @Size(max = 255)
        String email,

        @Schema(
                description = "User password",
                example = "Password@123",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minLength = 8,
                maxLength = 100,
                type = "string"
        )
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
