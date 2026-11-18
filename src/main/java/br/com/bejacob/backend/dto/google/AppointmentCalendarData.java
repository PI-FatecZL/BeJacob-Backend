package br.com.bejacob.backend.dto.google;

import br.com.bejacob.backend.entity.Appointment;
import java.time.LocalDateTime;

public record AppointmentCalendarData(
        Long appointmentId,
        String clientName,
        String clientPhone,
        String serviceName,
        LocalDateTime startsAt,
        LocalDateTime endsAt
) {
    public static AppointmentCalendarData from(Appointment appointment) {
        String clientName = appointment.getUser() != null ? appointment.getUser().getName() : "";
        String clientPhone = appointment.getUser() != null ? appointment.getUser().getPhone() : "";
        String serviceName = appointment.getService() != null ? appointment.getService().getName() : "";

        return new AppointmentCalendarData(
                appointment.getId(),
                clientName,
                clientPhone,
                serviceName,
                appointment.getStartsAt(),
                appointment.getEndsAt()
        );
    }
}
