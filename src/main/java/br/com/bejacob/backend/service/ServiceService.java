package br.com.bejacob.backend.service;

import br.com.bejacob.backend.dto.service.ServiceRequest;
import br.com.bejacob.backend.dto.service.ServiceResponse;
import br.com.bejacob.backend.entity.Services;
import br.com.bejacob.backend.exception.service.ServiceAlreadyExistsException;
import br.com.bejacob.backend.exception.service.ServiceHasAppointmentsException;
import br.com.bejacob.backend.exception.service.ServiceNotFoundException;
import br.com.bejacob.backend.mapper.ServiceMapper;
import br.com.bejacob.backend.repository.AppointmentRepository;
import br.com.bejacob.backend.repository.ServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServiceService {

    private final ServiceRepository repository;
    private final AppointmentRepository appointmentRepository;
    private final ServiceMapper mapper;

    public ServiceService(ServiceRepository repository, AppointmentRepository appointmentRepository, ServiceMapper mapper) {
        this.repository = repository;
        this.appointmentRepository = appointmentRepository;
        this.mapper = mapper;
    }

    @Transactional
    public ServiceResponse create(ServiceRequest request) {
        if (repository.existsByNameIgnoreCase(request.name())) {
            throw new ServiceAlreadyExistsException("Já existe um serviço cadastrado com este nome.");
        }

        Services service = mapper.toEntity(request);
        Services saved = repository.save(service);
        return mapper.toResponse(saved);
    }

    // listar serviços
    @Transactional(readOnly = true)
    public List<ServiceResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // listar por id
    @Transactional(readOnly = true)
    public ServiceResponse findById(Long id) {
        Services service = repository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Serviço não encontrado"));
        return mapper.toResponse(service);
    }

    // atualizar serviços
    @Transactional
    public ServiceResponse update(Long id, ServiceRequest request) {
        Services service = repository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Serviço não encontrado"));

        if (!service.getName().equalsIgnoreCase(request.name()) && repository.existsByNameIgnoreCase(request.name())) {
            throw new ServiceAlreadyExistsException("Já existe um serviço cadastrado com este nome.");
        }

        mapper.updateEntityFromDto(request, service);
        Services saved = repository.save(service);
        return mapper.toResponse(saved);
    }

    // deletar serviços com validação de agendamento
    @Transactional
    public void delete(Long id) {
        Services service = repository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Serviço não encontrado"));

        boolean hasAppointments = appointmentRepository.existsByServiceId(id);
        if (hasAppointments) {
            throw new ServiceHasAppointmentsException("Não é possível excluir este serviço pois ele está vinculado a um ou mais agendamentos.");
        }

        repository.delete(service);
    }
}