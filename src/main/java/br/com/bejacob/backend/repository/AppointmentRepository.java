package br.com.bejacob.backend.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.bejacob.backend.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
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

    @Query("""
        SELECT COUNT(a) > 0
        FROM Appointment a
        WHERE a.startsAt < :endsAt
        AND a.endsAt > :startsAt
    """)
    boolean existsConflict(
            @Param("startsAt") LocalDateTime startsAt,
            @Param("endsAt") LocalDateTime endsAt
    );

    boolean existsByServiceId(Long serviceId);
}