package br.com.bejacob.backend.exception.user;

public class UserHasAppointmentsException extends RuntimeException {
    public UserHasAppointmentsException() {
        super("Não é possível excluir este cliente pois ele está vinculado a um agendamento futuro.");
    }

    public UserHasAppointmentsException(String message) {
        super(message);
    }
}