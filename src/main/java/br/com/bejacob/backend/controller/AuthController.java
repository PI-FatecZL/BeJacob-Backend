package br.com.bejacob.backend.controller;

import br.com.bejacob.backend.config.security.dto.DataToken;
import br.com.bejacob.backend.dto.auth.LoginRequest;
import br.com.bejacob.backend.dto.auth.RegisterUserRequest;
import br.com.bejacob.backend.dto.auth.RegisterUserResponse;
import br.com.bejacob.backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService service;

    public AuthController(
            AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(
            @RequestBody RegisterUserRequest request) {
        service.register(request);
//        RegisterUserResponse newUser = service.register(request);
//        URI uri = uriBuilder
//                .path("/user/{id}")
//                .buildAndExpand(newUser.id())
//                .toUri();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<DataToken> login(
            @RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }
}