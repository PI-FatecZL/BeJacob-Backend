package br.com.bejacob.backend.controller;

import br.com.bejacob.backend.config.security.dto.DataToken;
import br.com.bejacob.backend.dto.auth.LoginRequest;
import br.com.bejacob.backend.dto.auth.RegisterUserRequest;
import br.com.bejacob.backend.dto.auth.RegisterUserResponse;
import br.com.bejacob.backend.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @SecurityRequirements
    public ResponseEntity<RegisterUserResponse> register(
            @RequestBody RegisterUserRequest request) {

        service.register(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    @SecurityRequirements
    public ResponseEntity<DataToken> login(
            @RequestBody LoginRequest request) {

        return ResponseEntity.ok(service.login(request));
    }
}