package br.com.bejacob.backend.exception.notification;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException() {
        super("Notificação não encontrada.");
    }
    public NotificationNotFoundException(String message) {
        super(message);
    }
}
