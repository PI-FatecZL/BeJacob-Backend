package br.com.bejacob.backend.dto.appointment;

import br.com.bejacob.backend.entity.enums.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateAppointmentAdminRequest(

        @NotNull
        Long userId,

        @NotNull
        Long serviceId,

        @NotNull
        @Future
        LocalDateTime startsAt,

        String notes,

        AppointmentStatus status
) {
}