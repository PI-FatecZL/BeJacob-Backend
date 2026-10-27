package br.com.bejacob.backend.dto.revenue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record RevenueResponse(
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal totalRevenue,
        Long totalAppointments,
        List<RevenueByPaymentMethod> byPaymentMethod
) {
}
