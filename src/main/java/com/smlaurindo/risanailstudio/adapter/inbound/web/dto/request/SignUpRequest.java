package com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request;

import com.smlaurindo.risanailstudio.application.usecase.SignUp;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 255, message = "Email must be at most 255 characters")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]+$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
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
