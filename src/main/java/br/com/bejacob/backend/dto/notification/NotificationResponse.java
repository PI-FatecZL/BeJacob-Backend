package br.com.bejacob.backend.dto.notification;

import br.com.bejacob.backend.entity.Notification;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        Long appointmentId,
        String clientName,
        String serviceName,
        LocalDateTime appointmentDate,
        String message,
        boolean isRead,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime createdAt
) {
    public NotificationResponse(Notification notification) {
        this(
                notification.getId(),
                notification.getAppointment().getId(),
                notification.getAppointment().getUser().getName(),
                notification.getAppointment().getService().getName(),
                notification.getAppointment().getStartsAt(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
