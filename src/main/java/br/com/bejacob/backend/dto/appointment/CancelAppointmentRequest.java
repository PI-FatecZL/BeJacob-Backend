package br.com.bejacob.backend.dto.appointment;

import jakarta.validation.constraints.NotBlank;

public record CancelAppointmentRequest(

        @NotBlank(message = "O motivo do cancelamento é obrigatório")
        String cancellationReason
) {
}
