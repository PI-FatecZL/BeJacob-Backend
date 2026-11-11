package br.com.bejacob.backend.dto.service;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateServiceRequest(
        String name,

        String description,

        @Positive
        Integer durationMinutes,

        @DecimalMin(value = "0.0")
        BigDecimal price,

        Boolean active
) {
}
