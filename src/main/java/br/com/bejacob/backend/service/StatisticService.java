package br.com.bejacob.backend.service;

import br.com.bejacob.backend.dto.revenue.RevenueByPaymentMethod;
import br.com.bejacob.backend.dto.revenue.RevenueResponse;
import br.com.bejacob.backend.dto.statistic.ReportData;
import br.com.bejacob.backend.dto.statistic.ServiceVolumeReport;
import br.com.bejacob.backend.dto.statistic.TopUserReport;
import br.com.bejacob.backend.entity.Appointment;
import br.com.bejacob.backend.entity.User;
import br.com.bejacob.backend.entity.enums.AppointmentStatus;
import br.com.bejacob.backend.exception.report.ReportGenerationException;
import br.com.bejacob.backend.exception.revenue.InvalidRevenuePeriodException;
import br.com.bejacob.backend.repository.AppointmentRepository;
import br.com.bejacob.backend.repository.StatisticRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticService {

    private final StatisticRepository statisticRepository;
    private final AppointmentRepository appointmentRepository;
    private final TemplateEngine templateEngine;

    public StatisticService(
            StatisticRepository statisticRepository,
            AppointmentRepository appointmentRepository,
            TemplateEngine templateEngine) {
        this.statisticRepository = statisticRepository;
        this.appointmentRepository = appointmentRepository;
        this.templateEngine = templateEngine;
    }

    // RELATORIO FINANCEIRO

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

    // RELATORIO PDF

    @Transactional(readOnly = true)
    public ReportData buildReportData(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidRevenuePeriodException("A data inicial não pode ser posterior à data final.");
        }

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay();

        List<Appointment> appointments = statisticRepository.findAllWithServiceInPeriod(start, end);

        long totalAppointments = appointments.size();
        long totalCompleted = appointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.COMPLETED)
                .count();
        long totalCanceled = appointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.CANCELED)
                .count();

        List<ServiceVolumeReport> servicesByVolume = appointments.stream()
                .collect(Collectors.groupingBy(a -> a.getService().getName(), Collectors.counting()))
                .entrySet().stream()
                .map(e -> new ServiceVolumeReport(e.getKey(), e.getValue()))
                .sorted((s1, s2) -> Long.compare(s2.count(), s1.count()))
                .toList();

        List<TopUserReport> topUsers = appointments.stream()
                .filter(a -> a.getUser() != null)
                .collect(Collectors.groupingBy(Appointment::getUser))
                .entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size()))
                .limit(6)
                .map(entry -> {
                    User user = entry.getKey();
                    long total = entry.getValue().size();

                    String phone = user.getPhone();
                    String name = user.getName();
                    String email = user.getEmail();

                    return new TopUserReport(name, phone, email, total);
                })
                .toList();

        return new ReportData(
                startDate,
                endDate,
                totalAppointments,
                totalCompleted,
                totalCanceled,
                servicesByVolume,
                topUsers
        );
    }

    public byte[] generatePdf(ReportData data) {
        try {
            DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter dateTimeFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

            long maxCount = data.servicesByVolume().stream()
                    .mapToLong(ServiceVolumeReport::count)
                    .max()
                    .orElse(1L);

            Context context = new Context();
            context.setVariable("data", data);
            context.setVariable("startFormatted", data.startDate().format(dateFmt));
            context.setVariable("endFormatted", data.endDate().format(dateFmt));
            context.setVariable("generatedAt", LocalDateTime.now().format(dateTimeFmt));
            context.setVariable("maxServiceCount", maxCount);

            String htmlContent = templateEngine.process("statistics/report", context);

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                ITextRenderer renderer = new ITextRenderer();
                renderer.setDocumentFromString(htmlContent);
                renderer.layout();
                renderer.createPDF(outputStream);

                return outputStream.toByteArray();
            }

        } catch (Exception e) {
            throw new ReportGenerationException("Falha ao gerar o relatório em PDF.", e);
        }
    }
}
