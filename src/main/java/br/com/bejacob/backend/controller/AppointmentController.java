package br.com.bejacob.backend.controller;

import br.com.bejacob.backend.dto.appointment.AppointmentResponse;
import br.com.bejacob.backend.dto.appointment.CreateAppointmentAdminRequest;
import br.com.bejacob.backend.dto.appointment.CreateAppointmentRequest;
import br.com.bejacob.backend.dto.appointment.UpdateAppointmentRequest;
import br.com.bejacob.backend.entity.User;
import br.com.bejacob.backend.service.AppointmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
@SecurityRequirement(name = "bearer-key")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }


    // CRIAR AGENDAMENTO (CLIENTE LOGADO)
    @PostMapping
    public ResponseEntity<AppointmentResponse> create(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CreateAppointmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(appointmentService.createAppointment(user.getId(), request));
    }

    
    // CRIAR AGENDAMENTO (ADMIN)

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppointmentResponse> createAdmin(
            @RequestBody @Valid CreateAppointmentAdminRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(appointmentService.createAppointmentAdmin(request));
    }

  
    // CONSULTAR HORÁRIOS DISPONÍVEIS (PÚBLICO)
    @GetMapping("/availability")
    @SecurityRequirements
    public ResponseEntity<List<LocalTime>> getAvailableTimes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Long serviceId) {
        return ResponseEntity.ok(appointmentService.getAvailableTimes(date, serviceId));
    }


    // LISTAR MEUS AGENDAMENTOS (CLIENTE LOGADO)
    @GetMapping("/me")
    public ResponseEntity<Page<AppointmentResponse>> listMyAppointments(
            @AuthenticationPrincipal User user,
            @ParameterObject @PageableDefault(size = 10, sort = "startsAt") Pageable pageable) {
        return ResponseEntity.ok(appointmentService.listUserAppointments(user.getId(), pageable));
    }


    // LISTAR TODOS OS AGENDAMENTOS (ADMIN)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AppointmentResponse>> listAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @ParameterObject @PageableDefault(size = 10, sort = "startsAt") Pageable pageable) {
        return ResponseEntity.ok(appointmentService.listAllAppointments(date, pageable));
    }


    // DETALHES DE UM AGENDAMENTO
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.detailsAppointment(id));
    }

    // ATUALIZAR AGENDAMENTO
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppointmentResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateAppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.updateAppointment(id, request));
    }


    // CANCELAR AGENDAMENTO
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponse> cancel(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(id, user));
    }
}