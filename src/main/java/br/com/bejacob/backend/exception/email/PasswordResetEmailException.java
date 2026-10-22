package br.com.bejacob.backend.exception.email;

public class PasswordResetEmailException extends RuntimeException {
    public PasswordResetEmailException()  {
        super("Falha ao enviar e-mail de recuperação");
    }
    public PasswordResetEmailException(String message) {
        super(message);
    }

    public PasswordResetEmailException(String message, Throwable e) {
        super(message, e);
    }

    public PasswordResetEmailException(Throwable e) {
        super(e);
    }
}
