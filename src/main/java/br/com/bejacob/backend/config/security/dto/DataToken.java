package br.com.bejacob.backend.config.security.dto;

import br.com.bejacob.backend.entity.enums.UserRoles;

public record DataToken(
        String tokenJWT,
        Long id,
        String email,
        UserRoles role) {
}