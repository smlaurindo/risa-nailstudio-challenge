package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request;

import com.smlaurindo.risanailstudio.application.usecase.SignUp;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Data for new user registration")
public record SignUpRequest(
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
                description = "User password. Must contain at least one uppercase letter, one lowercase letter, one digit, and one special character",
                example = "Password@123",
                requiredMode = Schema.RequiredMode.REQUIRED,
                pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]+$",
                minLength = 8,
                maxLength = 100,
                type = "string"
        )
        @NotBlank()
        @Size(min = 8, max = 100)
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]+$")
        String password
) {
        public SignUpRequest {
            email = email == null ? null : email.trim().toLowerCase();
        }

        public static SignUp.SignUpInput toInput(SignUpRequest request) {
            return new SignUp.SignUpInput(
                    request.email(),
                    request.password()
            );
        }
}
