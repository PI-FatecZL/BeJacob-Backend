package br.com.bejacob.backend.exception.google;

public class GoogleCalendarIntegrationException extends RuntimeException {
    public GoogleCalendarIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}