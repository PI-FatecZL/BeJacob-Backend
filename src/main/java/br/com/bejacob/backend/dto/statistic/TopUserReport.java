package br.com.bejacob.backend.dto.statistic;

public record TopUserReport(
        String name,
        String phone,
        String email,
        Long totalAppointments) {
}
