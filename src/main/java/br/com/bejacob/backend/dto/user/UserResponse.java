package br.com.bejacob.backend.dto.user;

public record UserResponse(
        Long id,
        String name,
        String email,
        String phone,
        boolean isActive) {
}
