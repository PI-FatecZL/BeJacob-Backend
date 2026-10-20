package br.com.bejacob.backend.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UpdateMeRequest(
        String name,
        @Email
        String email,
        @Pattern(
                regexp = "^[1-9]{2}9[0-9]{8}$",
                message = "Telefone deve usar o formato (11) 9xxxx-xxxx, sem espaços entre os dígitos"
        )
        String phone

) {
}
