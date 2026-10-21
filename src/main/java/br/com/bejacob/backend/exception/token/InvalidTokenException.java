package br.com.bejacob.backend.exception.token;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("Token inválido");
    }

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable e) {
        super(message, e);
    }

    public InvalidTokenException(Throwable e) {
        super(e);
    }
}
