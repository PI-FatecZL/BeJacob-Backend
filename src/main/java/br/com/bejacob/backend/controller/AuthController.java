package br.com.bejacob.backend.controller;

import br.com.bejacob.backend.config.security.dto.DataToken;
import br.com.bejacob.backend.dto.MessageResponse;
import br.com.bejacob.backend.dto.auth.LoginRequest;
import br.com.bejacob.backend.dto.auth.RegisterUserRequest;
import br.com.bejacob.backend.dto.auth.RegisterUserResponse;
import br.com.bejacob.backend.dto.auth.UpdateMeRequest;
import br.com.bejacob.backend.dto.auth.UpdatePasswordRequest;
import br.com.bejacob.backend.dto.user.UserResponse;
import br.com.bejacob.backend.entity.User;
import br.com.bejacob.backend.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @SecurityRequirements
    public ResponseEntity<String> register(
            @RequestBody @Valid RegisterUserRequest request) {
        service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário Cadastrado com Sucesso");
    }

    @PostMapping("/login")
    @SecurityRequirements
    public ResponseEntity<DataToken> login(
            @RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<MessageResponse> updatePassword(
            @RequestBody @Valid UpdatePasswordRequest data,
            @AuthenticationPrincipal User user){
        service.updatePassword(data, user.getId());
        return ResponseEntity.ok(new MessageResponse("Senha Atualizada com Sucesso"));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(
            @AuthenticationPrincipal User user){
        return ResponseEntity.ok(service.getMe(user.getId()));
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateMe(
            @RequestBody @Valid UpdateMeRequest data,
            @AuthenticationPrincipal User user){
        return ResponseEntity.ok(service.updateMe(data, user.getId()));
    }
}