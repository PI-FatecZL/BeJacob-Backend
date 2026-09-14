package br.com.bejacob.backend.dto.service;

import br.com.bejacob.backend.entity.Service;

import java.math.BigDecimal;

public record ServiceResponse(
        Long id,
        String name,
        String description,
        Integer durationMinutes,
        BigDecimal price,
        Boolean active

) {

    public ServiceResponse(Service service) {
        this(
                service.getId(),
                service.getName(),
                service.getDescription(),
                service.getDurationMinutes(),
                service.getPrice(),
                service.getActive()
        );
    }
}