package br.com.bejacob.backend.exception.auth;

public class PhoneAlreadyExistsException extends RuntimeException {
    public PhoneAlreadyExistsException () {
        super("Telefone já cadastrado");
    }
    public PhoneAlreadyExistsException(String message) {
        super(message);
    }
}