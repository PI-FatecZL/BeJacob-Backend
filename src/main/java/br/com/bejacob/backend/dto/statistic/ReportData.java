package br.com.bejacob.backend.dto.statistic;

import java.time.LocalDate;
import java.util.List;

public record ReportData(
        LocalDate startDate,
        LocalDate endDate,
        Long totalAppointments,
        Long totalCompleted,
        Long totalCanceled,
        List<ServiceVolumeReport> servicesByVolume) {
}
