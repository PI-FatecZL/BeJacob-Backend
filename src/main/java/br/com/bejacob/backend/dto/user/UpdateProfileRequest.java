package br.com.bejacob.backend.dto.user;

public record UpdateProfileRequest(
        String name,
        String email,
        String phone,
        Boolean isActive) {
}
