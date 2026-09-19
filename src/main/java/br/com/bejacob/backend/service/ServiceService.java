package br.com.bejacob.backend.service;

import br.com.bejacob.backend.dto.service.ServiceRequest;
import br.com.bejacob.backend.entity.Service;
import br.com.bejacob.backend.exception.service.ServiceNotFoundException;
import br.com.bejacob.backend.repository.ServiceRepository;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository repository;

    public ServiceService(ServiceRepository repository) {
        this.repository = repository;
    }

    // criar o serviço
    public Service create(ServiceRequest request) {

        Service service = new Service();

        service.setName(request.name());
        service.setDescription(request.description());
        service.setDurationMinutes(request.durationMinutes());
        service.setPrice(request.price());
        service.setActive(request.active());

        return repository.save(service);
    }

    // listar os serviços
    public List<Service> findAll() {
        return repository.findAll();
    }

    // buscar serviço por ID
    public Service findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Serviço não encontrado"));
    }

    // atualizar o serviço
    public Service update(Long id, ServiceRequest request) {

        Service existingService = findById(id);

        existingService.setName(request.name());
        existingService.setDescription(request.description());
        existingService.setDurationMinutes(request.durationMinutes());
        existingService.setPrice(request.price());
        existingService.setActive(request.active());

        return repository.save(existingService);
    }

    // deletar o serviço
    public void delete(Long id) {

        Service service = findById(id);

        repository.delete(service);
    }
}