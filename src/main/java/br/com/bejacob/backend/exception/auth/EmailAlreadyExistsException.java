package br.com.bejacob.backend.exception.auth;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException()  {
        super("Email já cadastrado");
    }
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
