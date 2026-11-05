package br.com.bejacob.backend.mapper;

import br.com.bejacob.backend.dto.notification.NotificationResponse;
import br.com.bejacob.backend.entity.Appointment;
import br.com.bejacob.backend.entity.Notification;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class NotificationMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public Notification toEntity(Appointment appointment) {
        String message = String.format(
                "Novo agendamento: %s agendou %s para %s às %s.",
                appointment.getUser().getName(),
                appointment.getService().getName(),
                appointment.getStartsAt().format(DATE_FORMATTER),
                appointment.getStartsAt().format(TIME_FORMATTER)
        );
        return new Notification(appointment, message);
    }

    public NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(notification);
    }
}
