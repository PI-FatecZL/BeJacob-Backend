package br.com.bejacob.backend.exception.appointment;

public class PaymentMethodRequiredException extends RuntimeException {
    public PaymentMethodRequiredException() {
        super("Informe a forma de pagamento para concluir o agendamento.");
    }
    public PaymentMethodRequiredException(String message) {
        super(message);
    }
}
