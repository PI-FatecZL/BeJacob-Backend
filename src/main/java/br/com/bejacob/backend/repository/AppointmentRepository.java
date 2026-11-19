package br.com.bejacob.backend.repository;
import br.com.bejacob.backend.dto.revenue.RevenueByPaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.bejacob.backend.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Page<Appointment> findByStartsAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<Appointment> findByUserId(Long userId, Pageable pageable);
    @Query("""
        SELECT a
        FROM Appointment a
        WHERE a.startsAt >= :startOfDay
        AND a.startsAt < :endOfDay
    """)
    List<Appointment> findAppointmentsOfDay(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    boolean existsByServiceId(Long serviceId);
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "CALL sp_complete_appointment(CAST(:id AS BIGINT), CAST(:paymentMethod AS VARCHAR), CAST(:referenceTime AS TIMESTAMP))", nativeQuery = true)
    void completeAppointment(
            @Param("id") Long id,
            @Param("paymentMethod") String paymentMethod,
            @Param("referenceTime") LocalDateTime referenceTime
    );

    @Query("""
        SELECT COUNT(a) > 0
        FROM Appointment a
        WHERE a.user.id = :userId
        AND a.startsAt >= :currentDateTime
        AND a.status = br.com.bejacob.backend.entity.enums.AppointmentStatus.SCHEDULED
    """)
    boolean existsFutureScheduledAppointmentsByUserId(
            @Param("userId") Long userId,
            @Param("currentDateTime") LocalDateTime currentDateTime
    );

    @Query("""
        SELECT new br.com.bejacob.backend.dto.revenue.RevenueByPaymentMethod(
            a.paymentMethod, SUM(a.price), COUNT(a)
        )
        FROM Appointment a
        WHERE a.status = br.com.bejacob.backend.entity.enums.AppointmentStatus.COMPLETED
        AND a.startsAt >= :start
        AND a.startsAt < :end
        GROUP BY a.paymentMethod
    """)
    List<RevenueByPaymentMethod> sumRevenueByPaymentMethod(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}