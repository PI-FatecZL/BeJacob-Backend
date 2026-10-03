package br.com.bejacob.backend.dto.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateAppointmentRequest(

        @NotNull
        Long serviceId,

        @NotNull
        @Future
        LocalDateTime startsAt,

        String notes
) {
}