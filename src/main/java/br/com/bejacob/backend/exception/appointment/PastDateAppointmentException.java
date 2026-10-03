package br.com.bejacob.backend.exception.appointment;

public class PastDateAppointmentException extends RuntimeException {
    public PastDateAppointmentException() {
        super("Não é permitido realizar ou atualizar agendamentos para datas ou horários passados.");
    }
    public PastDateAppointmentException(String message) {
        super(message);
    }
}