package br.com.bejacob.backend.dto.appointment;

import br.com.bejacob.backend.entity.enums.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record UpdateAppointmentRequest(
        Long serviceId,

      
        LocalDateTime startsAt,

        String notes,
        AppointmentStatus status
) {
}