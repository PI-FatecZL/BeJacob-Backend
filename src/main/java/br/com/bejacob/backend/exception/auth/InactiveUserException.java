package br.com.bejacob.backend.exception.auth;

public class InactiveUserException extends RuntimeException {
    public InactiveUserException() {
        super("Usuário inativo");
    }
    public InactiveUserException(String message) {
        super(message);
    }
}
