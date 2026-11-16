package br.com.bejacob.backend.exception.google;

public class GoogleCalendarEventNotFoundException extends RuntimeException {
    public GoogleCalendarEventNotFoundException(String message) {
        super(message);
    }
}