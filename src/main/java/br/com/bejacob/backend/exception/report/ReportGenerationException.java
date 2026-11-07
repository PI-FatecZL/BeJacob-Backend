package br.com.bejacob.backend.exception.report;

public class ReportGenerationException extends RuntimeException {
    public ReportGenerationException(String message) {
        super(message);
    }
    public ReportGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
