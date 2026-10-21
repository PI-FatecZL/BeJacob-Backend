package br.com.bejacob.backend.exception.token;

public class InvalidTokenIssuerException extends RuntimeException {
    public InvalidTokenIssuerException() {
        super("Issuer inválido");
    }

    public InvalidTokenIssuerException(String message) {
        super(message);
    }

    public InvalidTokenIssuerException(String message, Throwable e) {
        super(message, e);
    }

    public InvalidTokenIssuerException(Throwable e) {
        super(e);
    }
}
