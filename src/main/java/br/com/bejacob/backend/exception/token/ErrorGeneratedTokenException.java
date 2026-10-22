package br.com.bejacob.backend.exception.token;

public class ErrorGeneratedTokenException extends RuntimeException {
    public ErrorGeneratedTokenException() {
        super("Erro ao gerar Token JWT!");
    }

    public ErrorGeneratedTokenException(String message) {
        super(message);
    }

    public ErrorGeneratedTokenException(String message, Throwable e) {
        super(message, e);
    }

    public ErrorGeneratedTokenException(Throwable e) {
        super(e);
    }
}
