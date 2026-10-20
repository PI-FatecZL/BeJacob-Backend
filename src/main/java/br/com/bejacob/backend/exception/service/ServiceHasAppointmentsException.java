package br.com.bejacob.backend.exception.service;

public class ServiceHasAppointmentsException extends RuntimeException {
    public ServiceHasAppointmentsException() {
        super("Não é possível excluir este serviço pois ele está vinculado a um ou mais agendamentos.");
    }

    public ServiceHasAppointmentsException(String message) {
        super(message);
    }
}