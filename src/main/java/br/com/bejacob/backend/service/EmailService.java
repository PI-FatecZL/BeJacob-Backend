package br.com.bejacob.backend.service;

import br.com.bejacob.backend.exception.email.PasswordResetEmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async("emailSendExecutor")
    public void sendPasswordResetEmail(String toEmail, String userName, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(senderEmail);
            helper.setTo(toEmail);
            helper.setSubject("BeJacob Redefinição da Senha");

            String greeting = (userName != null && !userName.isBlank()) ? "Olá, " + userName : "Olá";

            String html = """
                    <div style="font-family: Arial, sans-serif; max-width: 580px; margin: 0 auto; padding: 24px; border: 1px solid #e0e0e0; border-radius: 8px;">
                        <h2 style="color: #222; text-align: center;">Recuperação de Senha</h2>
                        <p>%s,</p>
                        <p>Sua solicitação para redefinir a sua senha no em BeJacob (expira em 15 minutos).</p>
                        <p>Clique no botão abaixo para escolher uma nova senha:</p>
                        <div style="text-align: center; margin: 32px 0;">
                            <a href="%s" style="background-color: #1a73e8; color: #ffffff; padding: 12px 24px; text-decoration: none; border-radius: 6px; font-weight: bold; display: inline-block;">
                                Redefinir Senha
                            </a>
                        </div>
                    </div>
                    """.formatted(greeting, resetLink);

            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new PasswordResetEmailException("Falha ao enviar e-mail de recuperação", e);
        }
    }
}
