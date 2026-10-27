package br.com.bejacob.backend.dto.revenue;

import br.com.bejacob.backend.entity.enums.PaymentMethod;

import java.math.BigDecimal;

public record RevenueByPaymentMethod(
        PaymentMethod paymentMethod,
        BigDecimal total,
        Long appointments
) {
}
