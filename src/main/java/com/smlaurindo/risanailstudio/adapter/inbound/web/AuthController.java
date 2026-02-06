package com.smlaurindo.risanailstudio.adapter.inbound.web;

import com.smlaurindo.risanailstudio.adapter.inbound.web.dto.request.SignUpRequest;
import com.smlaurindo.risanailstudio.application.usecase.SignUp;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final SignUp signUp;

    @PostMapping(value = "/auth/sign-up", version = "1")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest request) {
        log.info("Sign up request received for email: {}", request.email());

        var input = SignUpRequest.toInput(request);

        signUp.signUp(input);

        log.info("Sign up successful for email: {}", request.email());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
