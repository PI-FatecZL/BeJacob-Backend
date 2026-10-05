package br.com.bejacob.backend.dto.appointment;

import br.com.bejacob.backend.dto.service.ServiceResponse;
import br.com.bejacob.backend.dto.user.UserResponse;
import br.com.bejacob.backend.entity.Appointment;
import br.com.bejacob.backend.entity.enums.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record AppointmentResponse(

        Long id,
        UserResponse user,
        ServiceResponse service,

       
        LocalDateTime startsAt,

    
        LocalDateTime endsAt,

        String notes,
        AppointmentStatus status,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime createdAt
) {
    public AppointmentResponse(Appointment appointment) {
        this(
                appointment.getId(),
                new UserResponse(
                        appointment.getUser().getId(),
                        appointment.getUser().getName(),
                        appointment.getUser().getEmail(),
                        appointment.getUser().getPhone(),
                        appointment.getUser().isActive()
                ),
                new ServiceResponse(appointment.getService()),
                appointment.getStartsAt(),
                appointment.getEndsAt(),
                appointment.getNotes(),
                appointment.getStatus(),
                appointment.getCreatedAt()
        );
    }
}