package br.com.bejacob.backend.exception.appointment;

public class AppointmentUnauthorizedException extends RuntimeException {
    public AppointmentUnauthorizedException() {
        super("Você não tem permissão para realizar esta operação neste agendamento.");
    }
    public AppointmentUnauthorizedException(String message) {
        super(message);
    }
}