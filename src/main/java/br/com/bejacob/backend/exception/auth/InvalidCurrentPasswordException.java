package br.com.bejacob.backend.exception.auth;

public class InvalidCurrentPasswordException extends RuntimeException {
    public InvalidCurrentPasswordException()  {
        super("Nova Senha não pode ser igual a antiga");
    }
    public InvalidCurrentPasswordException(String message) {
        super(message);
    }
}
