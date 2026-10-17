package br.com.bejacob.backend.exception.service;

public class ServiceAlreadyExistsException extends RuntimeException {
    public ServiceAlreadyExistsException() {
        super("Já existe um serviço cadastrado com este nome.");
    }

    public ServiceAlreadyExistsException(String message) {
        super(message);
    }
}