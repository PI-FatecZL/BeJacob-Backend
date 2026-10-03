package br.com.bejacob.backend.dto.service;

import br.com.bejacob.backend.entity.Services;

import java.math.BigDecimal;

public record ServiceResponse(
        Long id,
        String name,
        String description,
        Integer durationMinutes,
        BigDecimal price

) {

    public ServiceResponse(Services service) {
        this(
                service.getId(),
                service.getName(),
                service.getDescription(),
                service.getDurationMinutes(),
                service.getPrice()
        );
    }
}