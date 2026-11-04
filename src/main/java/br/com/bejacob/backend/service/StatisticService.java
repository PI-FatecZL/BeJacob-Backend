package br.com.bejacob.backend.service;

import br.com.bejacob.backend.dto.statistic.ReportData;
import br.com.bejacob.backend.dto.statistic.ServiceVolumeReport;
import br.com.bejacob.backend.entity.Appointment;
import br.com.bejacob.backend.entity.enums.AppointmentStatus;
import br.com.bejacob.backend.exception.report.ReportGenerationException;
import br.com.bejacob.backend.exception.revenue.InvalidRevenuePeriodException;
import br.com.bejacob.backend.repository.StatisticRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticService {

    private final StatisticRepository statisticRepository;
    private final TemplateEngine templateEngine;

    public StatisticService(
            StatisticRepository statisticRepository,
            TemplateEngine templateEngine) {
        this.statisticRepository = statisticRepository;
        this.templateEngine = templateEngine;
    }

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

        return new ReportData(
                startDate,
                endDate,
                totalAppointments,
                totalCompleted,
                totalCanceled,
                servicesByVolume
        );
    }

    public byte[] generatePdf(ReportData data) {
        try {
            DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter dateTimeFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

            // Calcula a largura percentual das barras com base no maior valor
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

            // Renderiza o PDF
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
