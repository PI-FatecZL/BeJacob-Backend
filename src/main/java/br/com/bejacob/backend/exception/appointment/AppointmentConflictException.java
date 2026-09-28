package br.com.bejacob.backend.exception.appointment;

public class AppointmentConflictException extends RuntimeException {
    public AppointmentConflictException() {
        super("Já existe um agendamento para este horário ou o horário colide com outro existente.");
    }
    public AppointmentConflictException(String message) {
        super(message);
    }
}