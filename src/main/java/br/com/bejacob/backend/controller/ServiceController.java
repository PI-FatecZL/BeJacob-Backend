package br.com.bejacob.backend.controller;

import br.com.bejacob.backend.dto.service.ServiceRequest;
import br.com.bejacob.backend.dto.service.ServiceResponse;
import br.com.bejacob.backend.service.ServiceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
public class ServiceController {

    private final ServiceService service;

    public ServiceController(ServiceService service) {
        this.service = service;
    }

    // Criar o serviço(apenas admin)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceResponse> create(
            @RequestBody @Valid ServiceRequest request) {

        return ResponseEntity.ok(
                new ServiceResponse(service.create(request))
        );
    }

    // listar os serviços
    @GetMapping
    public ResponseEntity<List<ServiceResponse>> findAll() {
        return ResponseEntity.ok(
                service.findAll()
                        .stream()
                        .map(ServiceResponse::new)
                        .toList()
        );
    }

    // buscar serviço por ID
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> findById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                new ServiceResponse(service.findById(id))
        );
    }

    // atualizar parcialmente o serviço
    @PatchMapping("/{id}")
    public ResponseEntity<ServiceResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid ServiceRequest request) {
        return ResponseEntity.ok(
                new ServiceResponse(service.update(id, request))
        );
    }

    // deletar o serviço
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}