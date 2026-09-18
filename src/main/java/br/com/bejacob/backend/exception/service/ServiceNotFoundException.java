package br.com.bejacob.backend.exception.service;

public class ServiceNotFoundException extends RuntimeException {
    public ServiceNotFoundException() {
        super("Serviço não encontrado");
    }
    public ServiceNotFoundException(String message) {
        super(message);
    }
}
