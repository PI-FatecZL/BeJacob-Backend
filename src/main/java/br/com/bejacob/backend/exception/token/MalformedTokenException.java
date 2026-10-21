package br.com.bejacob.backend.exception.token;

public class MalformedTokenException extends RuntimeException {
    public MalformedTokenException() {
        super("Token malformado");
    }

    public MalformedTokenException(String message) {
        super(message);
    }

    public MalformedTokenException(String message, Throwable e) {
        super(message, e);
    }

    public MalformedTokenException(Throwable e) {
        super(e);
    }
}
