package br.com.bejacob.backend.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdatePasswordRequest(
        @NotBlank
        String oldPassword,

        @NotBlank
        String newPassword) {
}
