package br.com.bejacob.backend.dto.auth.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(
        @NotBlank(message = "Email é obrigatorio")
        @Email
        String email ) {
}
