package br.com.bejacob.backend.service;

import br.com.bejacob.backend.dto.appointment.AppointmentResponse;
import br.com.bejacob.backend.dto.appointment.CreateAppointmentAdminRequest;
import br.com.bejacob.backend.dto.appointment.CreateAppointmentRequest;
import br.com.bejacob.backend.dto.appointment.UpdateAppointmentRequest;
import br.com.bejacob.backend.entity.Appointment;
import br.com.bejacob.backend.entity.Services;
import br.com.bejacob.backend.entity.User;
import br.com.bejacob.backend.entity.enums.AppointmentStatus;
import br.com.bejacob.backend.exception.appointment.AppointmentConflictException;
import br.com.bejacob.backend.exception.appointment.AppointmentUnauthorizedException;
import br.com.bejacob.backend.exception.appointment.PastDateAppointmentException;
import br.com.bejacob.backend.exception.service.ServiceNotFoundException;
import br.com.bejacob.backend.exception.user.UserNotFoundException;
import br.com.bejacob.backend.mapper.AppointmentMapper;
import br.com.bejacob.backend.repository.AppointmentRepository;
import br.com.bejacob.backend.repository.ServiceRepository;
import br.com.bejacob.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final AppointmentMapper mapper;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            ServiceRepository serviceRepository,
            UserRepository userRepository,
            AppointmentMapper mapper) {
        this.appointmentRepository = appointmentRepository;
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    // 1. CRIAR AGENDAMENTO (CLIENTE)
    @Transactional
    public AppointmentResponse createAppointment(Long userId, CreateAppointmentRequest request) {
        User user = findUserById(userId);
        Services serviceEntity = findActiveServiceById(request.serviceId());

        LocalDateTime startsAt = request.startsAt();
        validatePastDate(startsAt);

        LocalDateTime endsAt = startsAt.plusMinutes(serviceEntity.getDurationMinutes());

        validateNoConflict(startsAt, endsAt);

        Appointment appointment = mapper.toEntity(request, user, serviceEntity, endsAt);
        Appointment saved = appointmentRepository.save(appointment);

        return mapper.toResponse(saved);
    }

    // 2. CRIAR AGENDAMENTO (ADMIN)
    @Transactional
    public AppointmentResponse createAppointmentAdmin(CreateAppointmentAdminRequest request) {
        User user = findUserById(request.userId());
        Services serviceEntity = findServiceById(request.serviceId());

        LocalDateTime startsAt = request.startsAt();
        validatePastDate(startsAt);

        LocalDateTime endsAt = startsAt.plusMinutes(serviceEntity.getDurationMinutes());

        validateNoConflict(startsAt, endsAt);

        Appointment appointment = mapper.toEntityAdmin(request, user, serviceEntity, endsAt);
        Appointment saved = appointmentRepository.save(appointment);

        return mapper.toResponse(saved);
    }

    // 3. ATUALIZAR AGENDAMENTO
    @Transactional
    public AppointmentResponse updateAppointment(Long id, UpdateAppointmentRequest request) {
        Appointment appointment = findAppointmentById(id);

        Services updatedService = appointment.getService();
        if (request.serviceId() != null) {
            updatedService = findServiceById(request.serviceId());
        }

        LocalDateTime newStartsAt = request.startsAt() != null ? request.startsAt() : appointment.getStartsAt();
        
        if (request.startsAt() != null) {
            validatePastDate(newStartsAt);
        }

        LocalDateTime newEndsAt = appointment.getEndsAt();

        if (request.startsAt() != null || request.serviceId() != null) {
            newEndsAt = newStartsAt.plusMinutes(updatedService.getDurationMinutes());
            validateNoConflictForUpdate(appointment.getId(), newStartsAt, newEndsAt);
        }

        mapper.updateEntityFromDto(request, updatedService, newStartsAt, newEndsAt, appointment);

        Appointment saved = appointmentRepository.save(appointment);
        return mapper.toResponse(saved);
    }

    // 4. CANCELAR AGENDAMENTO (COM VALIDAÇÃO DE PERMISSÃO)
    @Transactional
    public AppointmentResponse cancelAppointment(Long id, User authenticatedUser) {
        Appointment appointment = findAppointmentById(id);

        boolean isAdmin = authenticatedUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isOwner = appointment.getUser().getId().equals(authenticatedUser.getId());

        if (!isAdmin && !isOwner) {
            throw new AppointmentUnauthorizedException("Você não tem permissão para cancelar este agendamento.");
        }

        appointment.cancel();
        Appointment saved = appointmentRepository.save(appointment);
        return mapper.toResponse(saved);
    }

    // 5. DETALHES DE UM AGENDAMENTO
    @Transactional(readOnly = true)
    public AppointmentResponse detailsAppointment(Long id) {
        Appointment appointment = findAppointmentById(id);
        return mapper.toResponse(appointment);
    }

    // 6. LISTAR MEUS AGENDAMENTOS (CLIENTE LOGADO)
    @Transactional(readOnly = true)
    public Page<AppointmentResponse> listUserAppointments(Long userId, Pageable pageable) {
        return appointmentRepository.findByUserId(userId, pageable)
                .map(mapper::toResponse);
    }

    // 7. LISTAR TODOS OS AGENDAMENTOS COM FILTRO DE DATA (ADMIN)
    @Transactional(readOnly = true)
    public Page<AppointmentResponse> listAllAppointments(LocalDate date, Pageable pageable) {
        if (date != null) {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
            return appointmentRepository.findByStartsAtBetween(startOfDay, endOfDay, pageable)
                    .map(mapper::toResponse);
        }
        return appointmentRepository.findAll(pageable)
                .map(mapper::toResponse);
    }

    // 8. CONSULTAR HORÁRIOS DISPONÍVEIS
    @Transactional(readOnly = true)
    public List<LocalTime> getAvailableTimes(LocalDate date, Long serviceId) {
        Services serviceEntity = findActiveServiceById(serviceId);

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        List<Appointment> existingAppointments = appointmentRepository.findAppointmentsOfDay(startOfDay, endOfDay);

        LocalDateTime currentSlot = date.atTime(8, 0);
        LocalDateTime closingTime = date.atTime(20, 0);

        List<LocalTime> availableTimes = new ArrayList<>();

        while (!currentSlot.plusMinutes(serviceEntity.getDurationMinutes()).isAfter(closingTime)) {
            LocalDateTime slotEnd = currentSlot.plusMinutes(serviceEntity.getDurationMinutes());
            final LocalDateTime checkStart = currentSlot;

            boolean hasConflict = existingAppointments.stream()
                    .filter(a -> a.getStatus() != AppointmentStatus.CANCELED)
                    .anyMatch(a -> checkStart.isBefore(a.getEndsAt()) && slotEnd.isAfter(a.getStartsAt()));

        
            boolean isPastTime = currentSlot.isBefore(LocalDateTime.now());

            if (!hasConflict && !isPastTime) {
                availableTimes.add(currentSlot.toLocalTime());
            }

            currentSlot = currentSlot.plusMinutes(30);
        }

        return availableTimes;
    }

  
    private void validatePastDate(LocalDateTime startsAt) {
        if (startsAt.isBefore(LocalDateTime.now())) {
            throw new PastDateAppointmentException("Não é permitido realizar ou atualizar agendamentos para datas ou horários passados.");
        }
    }

    private void validateNoConflict(LocalDateTime startsAt, LocalDateTime endsAt) {
        if (appointmentRepository.existsConflict(startsAt, endsAt)) {
            throw new AppointmentConflictException("Já existe um agendamento para este horário.");
        }
    }

    private void validateNoConflictForUpdate(Long currentAppointmentId, LocalDateTime startsAt, LocalDateTime endsAt) {
        boolean exists = appointmentRepository.findAll().stream()
                .filter(a -> !a.getId().equals(currentAppointmentId))
                .filter(a -> a.getStatus() != AppointmentStatus.CANCELED)
                .anyMatch(a -> a.getStartsAt().isBefore(endsAt) && a.getEndsAt().isAfter(startsAt));

        if (exists) {
            throw new AppointmentConflictException("O novo horário solicitado colide com outro agendamento existente.");
        }
    }

    private User findUserById(Long userId) {
        return userRepository.findByIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));
    }

    private Services findServiceById(Long serviceId) {
        return serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ServiceNotFoundException("Serviço não encontrado."));
    }

    private Services findActiveServiceById(Long serviceId) {
        Services serviceEntity = findServiceById(serviceId);
        if (Boolean.FALSE.equals(serviceEntity.getActive())) {
            throw new IllegalArgumentException("Este serviço não está disponível para agendamento.");
        }
        return serviceEntity;
    }

    private Appointment findAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado."));
    }
}