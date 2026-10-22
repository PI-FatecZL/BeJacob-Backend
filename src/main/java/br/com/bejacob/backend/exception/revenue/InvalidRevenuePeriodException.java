package br.com.bejacob.backend.exception.revenue;

public class InvalidRevenuePeriodException extends RuntimeException {
    public InvalidRevenuePeriodException() {
        super("A data inicial não pode ser posterior à data final.");
    }
    public InvalidRevenuePeriodException(String message) {
        super(message);
    }
}
