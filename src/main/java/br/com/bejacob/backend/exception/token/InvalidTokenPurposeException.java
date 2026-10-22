package br.com.bejacob.backend.exception.token;

public class InvalidTokenPurposeException extends RuntimeException {
    public InvalidTokenPurposeException() {
        super("Finalidade do token inválida");
    }

    public InvalidTokenPurposeException(String message) {
        super(message);
    }

    public InvalidTokenPurposeException(String message, Throwable e) {
        super(message, e);
    }

    public InvalidTokenPurposeException(Throwable e) {
        super(e);
    }
}
