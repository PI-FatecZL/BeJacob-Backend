package br.com.bejacob.backend.exception.appointment;

public class InvalidAppointmentTimeException extends RuntimeException {
    public InvalidAppointmentTimeException() {
        super("Horário de agendamento inválido.");
    }

    public InvalidAppointmentTimeException(String message) {
        super(message);
    }
}