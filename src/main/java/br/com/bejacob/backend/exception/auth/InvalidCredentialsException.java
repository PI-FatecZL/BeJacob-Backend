package br.com.bejacob.backend.exception.auth;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("E-mail ou senha inválidos");
    }
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
