package br.com.bejacob.backend.service;

import br.com.bejacob.backend.entity.Appointment;
import br.com.bejacob.backend.entity.AppointmentGoogleEvent;
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
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class GoogleCalendarService {

    private static final Logger log = LoggerFactory.getLogger(GoogleCalendarService.class.getName());
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

    // CRIAR EVENTO NO GOOGLE CALENDAR
    @Async
    public void createEvent(Appointment appointment) {
        try {
            Event event = buildEvent(appointment);
            Event created = calendar.events()
                    .insert(calendarId, event)
                    .execute();
            AppointmentGoogleEvent googleEvent = googleEventRepository
                    .findByAppointmentId(appointment.getId())
                    .orElseThrow();
            googleEvent.setGoogleEventId(created.getId());
            googleEventRepository.save(googleEvent);
            log.info(
                    "TESTE - Agendamento/Evento criado no Google Calendar: {} (agendamento #{})",
                    created.getId(), appointment.getId()
            );

        } catch (IOException exception) {
            throw new GoogleCalendarIntegrationException(
                    "Falha ao criar evento no Google Calendar para o agendamento #" + appointment.getId(),
                    exception
            );
        }
    }

    // ATUALIZA EVENTO NO GOOGLE CALENDAR
    @Async
    public void updateEvent(Appointment appointment) {

        String googleEventId = googleEventRepository
                .findByAppointmentId(appointment.getId())
                .map(AppointmentGoogleEvent::getGoogleEventId)
                .orElse(null);

        if (googleEventId == null) {
            log.warn(
                    "Agendamento #{} não tem evento no Google Calendar para atualizar",
                    appointment.getId()
            );
            return;
        }

        try {
            Event event = buildEvent(appointment);
            calendar.events()
                    .update(calendarId, googleEventId, event)
                    .execute();
            log.info(
                    "Evento atualizado no Google Calendar: {} (agendamento #{})",
                    googleEventId, appointment.getId()
            );
        } catch (IOException exception) {
            throw new GoogleCalendarIntegrationException(
                    "Falha ao atualizar evento no Google Calendar para o agendamento #" + appointment.getId(),
                    exception
            );
        }
    }

    // DELETE EVENTO NO GOOGLE CALENDAR
    @Async
    public void  deleteEvent(Appointment appointment) {
        String googleEventId = googleEventRepository
                .findByAppointmentId(appointment.getId())
                .map(AppointmentGoogleEvent::getGoogleEventId)
                .orElse(null);

        if (googleEventId == null) {
            log.warn(
                    "Agendamento #{} não tem evento no Google Calendar para deletar",
                    appointment.getId()
            );
            return;
        }

        try {
            calendar.events()
                    .delete(calendarId, googleEventId)
                    .execute();
            log.info(
                    "Evento deletado do Google Calendar: {} (agendamento #{})",
                    googleEventId, appointment.getId()
            );

        } catch (IOException exception) {
            throw new GoogleCalendarIntegrationException(
                    "Falha ao deletar evento no Google Calendar para o agendamento #" + appointment.getId(),
                    exception
            );
        }
    }

    private Event buildEvent(Appointment appointment) {
        Event event = new Event();
        event.setSummary(appointment.getUser().getName()+ " - " + appointment.getService().getName());

        StringBuilder description = new StringBuilder();
        description.append("Cliente: ").append(appointment.getUser().getName()).append("\n");
        description.append("Telefone: ").append(appointment.getUser().getPhone()).append("\n");
        description.append("Serviço: ").append(appointment.getService().getName()).append("\n");

        event.setDescription(description.toString());

        ZonedDateTime startZoned = appointment.getStartsAt().atZone(ZoneId.of(TIME_ZONE));
        EventDateTime start = new EventDateTime()
                .setDateTime(new DateTime(startZoned.toInstant().toEpochMilli()))
                .setTimeZone(TIME_ZONE);
        event.setStart(start);

        ZonedDateTime endZoned = appointment.getEndsAt().atZone(ZoneId.of(TIME_ZONE));
        EventDateTime end = new EventDateTime()
                .setDateTime(new DateTime(endZoned.toInstant().toEpochMilli()))
                .setTimeZone(TIME_ZONE);
        event.setEnd(end);

        return event;
    }


}
