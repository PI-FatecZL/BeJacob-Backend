package br.com.bejacob.backend.dto.auth;

import br.com.bejacob.backend.entity.User;
import br.com.bejacob.backend.entity.enums.UserRoles;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RegisterUserResponse(

        Long id,

        @NotNull
        String name,

        @NotNull
        @Email
        String email,

        @NotBlank
        String phone,

        @NotBlank
        UserRoles role,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime createdAt) {
}
