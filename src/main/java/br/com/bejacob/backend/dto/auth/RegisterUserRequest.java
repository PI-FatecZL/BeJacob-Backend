package br.com.bejacob.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterUserRequest(

        @NotNull
        String name,

        @NotNull
        @Email
        String email,

        @NotNull
        String password,

        @NotBlank
        String phone

) {
}