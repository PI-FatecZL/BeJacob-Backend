package br.com.bejacob.backend.dto.service;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ServiceRequest(

        @NotBlank
        String name,

        String description,

        @NotNull
        @Positive
        Integer durationMinutes,

        @NotNull
        @DecimalMin(value = "0.0")
        BigDecimal price
) {
}