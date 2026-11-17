package br.com.bejacob.backend.service;

import br.com.bejacob.backend.entity.AppointmentGoogleEvent;
import br.com.bejacob.backend.dto.google.AppointmentCalendarData;
import br.com.bejacob.backend.dto.google.AppointmentCanceledEvent;
import br.com.bejacob.backend.dto.google.AppointmentCreatedEvent;
import br.com.bejacob.backend.dto.google.AppointmentUpdatedEvent;
import br.com.bejacob.backend.exception.google.GoogleCalendarEventNotFoundException;
import br.com.bejacob.backend.exception.google.GoogleCalendarIntegrationException;
import br.com.bejacob.backend.repository.AppointmentGoogleEventRepository;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class GoogleCalendarService {

    private static final Logger log = LoggerFactory.getLogger(GoogleCalendarService.class);
    private static final String TIME_ZONE = "America/Sao_Paulo";
    private final Calendar calendar;
    private final String calendarId;
    private final AppointmentGoogleEventRepository googleEventRepository;

    public GoogleCalendarService(
            Calendar calendar,
            @Value("${google.calendar.id}") String calendarId,
            AppointmentGoogleEventRepository googleEventRepository) {
        this.calendar = calendar;
        this.calendarId = calendarId;
        this.googleEventRepository = googleEventRepository;
    }

    // CRIAR EVENTO NO GOOGLE CALENDAR (EXECUTADO APÓS O COMMIT DA TRANSAÇÃO)
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createEvent(AppointmentCreatedEvent event) {
        AppointmentCalendarData data = event.data();
        try {
            Event googleEventPayload = buildEvent(data);
            Event created = calendar.events()
                    .insert(calendarId, googleEventPayload)
                    .execute();

            AppointmentGoogleEvent googleEvent = googleEventRepository
                    .findByAppointmentId(data.appointmentId())
                    .orElseThrow(() -> new GoogleCalendarEventNotFoundException(
                            "Registro de evento Google não encontrado para o agendamento #" + data.appointmentId()
                    ));

            googleEvent.setGoogleEventId(created.getId());
            googleEventRepository.save(googleEvent);

            log.info(
                    "Agendamento/Evento criado no Google Calendar: {} (agendamento #{})",
                    created.getId(), data.appointmentId()
            );

        } catch (IOException exception) {
            log.error(
                    "Falha ao criar evento no Google Calendar para o agendamento #{}: {}",
                    data.appointmentId(), exception.getMessage(), exception
            );
            throw new GoogleCalendarIntegrationException(
                    "Falha ao criar evento no Google Calendar para o agendamento #" + data.appointmentId(),
                    exception
            );
        }
    }

    // ATUALIZA EVENTO NO GOOGLE CALENDAR (EXECUTADO APÓS O COMMIT DA TRANSAÇÃO)
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void updateEvent(AppointmentUpdatedEvent event) {
        AppointmentCalendarData data = event.data();

        String googleEventId = googleEventRepository
                .findByAppointmentId(data.appointmentId())
                .map(AppointmentGoogleEvent::getGoogleEventId)
                .orElse(null);

        if (googleEventId == null) {
            log.warn(
                    "Agendamento #{} não tem evento no Google Calendar para atualizar",
                    data.appointmentId()
            );
            return;
        }

        try {
            Event googleEventPayload = buildEvent(data);
            calendar.events()
                    .update(calendarId, googleEventId, googleEventPayload)
                    .execute();
            log.info(
                    "Evento atualizado no Google Calendar: {} (agendamento #{})",
                    googleEventId, data.appointmentId()
            );
        } catch (IOException exception) {
            log.error(
                    "Falha ao atualizar evento no Google Calendar para o agendamento #{}: {}",
                    data.appointmentId(), exception.getMessage(), exception
            );
            throw new GoogleCalendarIntegrationException(
                    "Falha ao atualizar evento no Google Calendar para o agendamento #" + data.appointmentId(),
                    exception
            );
        }
    }

    // DELETE EVENTO NO GOOGLE CALENDAR (EXECUTADO APÓS O COMMIT DA TRANSAÇÃO)
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void deleteEvent(AppointmentCanceledEvent event) {
        Long appointmentId = event.appointmentId();

        String googleEventId = googleEventRepository
                .findByAppointmentId(appointmentId)
                .map(AppointmentGoogleEvent::getGoogleEventId)
                .orElse(null);

        if (googleEventId == null) {
            log.warn(
                    "Agendamento #{} não tem evento no Google Calendar para deletar",
                    appointmentId
            );
            return;
        }

        try {
            calendar.events()
                    .delete(calendarId, googleEventId)
                    .execute();
            log.info(
                    "Evento deletado do Google Calendar: {} (agendamento #{})",
                    googleEventId, appointmentId
            );

        } catch (IOException exception) {
            log.error(
                    "Falha ao deletar evento no Google Calendar para o agendamento #{}: {}",
                    appointmentId, exception.getMessage(), exception
            );
            throw new GoogleCalendarIntegrationException(
                    "Falha ao deletar evento no Google Calendar para o agendamento #" + appointmentId,
                    exception
            );
        }
    }

    private Event buildEvent(AppointmentCalendarData data) {
        Event event = new Event();
        event.setSummary(data.clientName() + " - " + data.serviceName());

        StringBuilder description = new StringBuilder();
        description.append("Cliente: ").append(data.clientName()).append("\n");
        description.append("Telefone: ").append(data.clientPhone()).append("\n");
        description.append("Serviço: ").append(data.serviceName()).append("\n");

        event.setDescription(description.toString());

        ZonedDateTime startZoned = data.startsAt().atZone(ZoneId.of(TIME_ZONE));
        EventDateTime start = new EventDateTime()
                .setDateTime(new DateTime(startZoned.toInstant().toEpochMilli()))
                .setTimeZone(TIME_ZONE);
        event.setStart(start);

        ZonedDateTime endZoned = data.endsAt().atZone(ZoneId.of(TIME_ZONE));
        EventDateTime end = new EventDateTime()
                .setDateTime(new DateTime(endZoned.toInstant().toEpochMilli()))
                .setTimeZone(TIME_ZONE);
        event.setEnd(end);

        return event;
    }
}
