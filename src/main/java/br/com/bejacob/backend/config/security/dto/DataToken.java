package br.com.bejacob.backend.config.security.dto;

public record DataToken(
        String tokenJWT,
        Long id,
        String email) {
}