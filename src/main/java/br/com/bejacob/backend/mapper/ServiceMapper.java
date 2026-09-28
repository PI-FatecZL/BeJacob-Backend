package br.com.bejacob.backend.mapper;

import br.com.bejacob.backend.dto.service.ServiceRequest;
import br.com.bejacob.backend.dto.service.ServiceResponse;
import br.com.bejacob.backend.entity.Services;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {

    public Services toEntity(ServiceRequest request) {
        return new Services(request);
    }

    public ServiceResponse toResponse(Services service) {
        return new ServiceResponse(service);
    }

    public void updateEntityFromDto(ServiceRequest request, Services service) {
        service.updateDetails(request);
    }
}