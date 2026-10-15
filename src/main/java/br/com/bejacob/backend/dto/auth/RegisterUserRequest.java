package br.com.bejacob.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegisterUserRequest(

        @NotNull
        String name,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
                message = "A senha deve ter no mínimo 8 caracteres, conter pelo menos uma letra maiúscula e um caractere especial"
        )
        String password,

        @NotBlank
        @Pattern(
                regexp = "^[1-9]{2}9[0-9]{8}$",
                message = "Telefone deve usar o formato (11) 9xxxx-xxxx, sem espaços entre os dígitos"
        )
        String phone

) {
}