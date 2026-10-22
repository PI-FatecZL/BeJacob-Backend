package br.com.bejacob.backend.exception.token;

public class ExpiredOrInvalidJwtException extends RuntimeException {
    public ExpiredOrInvalidJwtException() {
        super("Token inválido ou expirado!");
    }

    public ExpiredOrInvalidJwtException(String message) {
        super(message);
    }

    public ExpiredOrInvalidJwtException(String message, Throwable e) {
        super(message, e);
    }

    public ExpiredOrInvalidJwtException(Throwable e) {
        super(e);
    }
}
