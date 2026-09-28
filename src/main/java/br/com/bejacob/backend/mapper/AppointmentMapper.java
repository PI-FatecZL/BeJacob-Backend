package br.com.bejacob.backend.mapper;

import br.com.bejacob.backend.dto.appointment.AppointmentResponse;
import br.com.bejacob.backend.dto.appointment.CreateAppointmentAdminRequest;
import br.com.bejacob.backend.dto.appointment.CreateAppointmentRequest;
import br.com.bejacob.backend.dto.appointment.UpdateAppointmentRequest;
import br.com.bejacob.backend.entity.Appointment;
import br.com.bejacob.backend.entity.Services;
import br.com.bejacob.backend.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AppointmentMapper {

    public Appointment toEntity(CreateAppointmentRequest request, User user, Services service, LocalDateTime endsAt) {
        return new Appointment(user, service, request.startsAt(), endsAt, request.notes());
    }

    public Appointment toEntityAdmin(CreateAppointmentAdminRequest request, User user, Services service, LocalDateTime endsAt) {
        return new Appointment(user, service, request.startsAt(), endsAt, request.notes(), request.status());
    }

    public AppointmentResponse toResponse(Appointment appointment) {
        return new AppointmentResponse(appointment);
    }

    public void updateEntityFromDto(
            UpdateAppointmentRequest request,
            Services service,
            LocalDateTime startsAt,
            LocalDateTime endsAt,
            Appointment appointment) {

        appointment.updateDetails(
                service,
                startsAt,
                endsAt,
                request.notes(),
                request.status()
        );
    }
}