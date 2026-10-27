package br.com.bejacob.backend.service;

import br.com.bejacob.backend.dto.revenue.RevenueByPaymentMethod;
import br.com.bejacob.backend.dto.revenue.RevenueResponse;
import br.com.bejacob.backend.exception.revenue.InvalidRevenuePeriodException;
import br.com.bejacob.backend.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class RevenueService {

    private final AppointmentRepository appointmentRepository;

    public RevenueService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

   
    @Transactional(readOnly = true)
    public RevenueResponse getRevenue(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidRevenuePeriodException("A data inicial não pode ser posterior à data final.");
        }

      
        List<RevenueByPaymentMethod> byPaymentMethod = appointmentRepository.sumRevenueByPaymentMethod(
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay()
        );

        BigDecimal totalRevenue = byPaymentMethod.stream()
                .map(RevenueByPaymentMethod::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long totalAppointments = byPaymentMethod.stream()
                .mapToLong(RevenueByPaymentMethod::appointments)
                .sum();

        return new RevenueResponse(startDate, endDate, totalRevenue, totalAppointments, byPaymentMethod);
    }
}
