package br.com.bejacob.backend.config.google;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.FileInputStream;
import java.util.Collections;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Configuration
public class GoogleCalendarConfig {

    @Value("${google.credentials.path}")
    private String googleCredentials;

    @Bean
    public Calendar googleCalendarClient()  throws GeneralSecurityException, IOException {
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new FileInputStream(googleCredentials))
                .createScoped(Collections.singletonList(CalendarScopes.CALENDAR));

        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName("Bejacob-Backend")
                .build();
    }
}
