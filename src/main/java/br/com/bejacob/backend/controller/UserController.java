package br.com.bejacob.backend.controller;

import br.com.bejacob.backend.dto.user.UpdateProfileRequest;
import br.com.bejacob.backend.dto.user.UserResponse;
import br.com.bejacob.backend.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearer-key")
public class UserController {

    private final UserService service;

    public UserController(
            UserService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> findAll(){
        return ResponseEntity.ok(service.listAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(
            @PathVariable Long id){
        return ResponseEntity.ok(service.detailsUser(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateProfileRequest data){
        return ResponseEntity.ok(service.updateUser(id, data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> delete(
            @PathVariable Long id){
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
