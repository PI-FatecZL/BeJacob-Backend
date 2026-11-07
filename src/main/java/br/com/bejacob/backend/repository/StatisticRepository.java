package br.com.bejacob.backend.repository;

import br.com.bejacob.backend.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<Appointment, Long> {

    /**
     * Busca todos os agendamentos do período com o Serviço e o Usuário associados (Eager fetch),
     * permitindo agregações de serviços e ranking de clientes em memória sem N+1 queries.
     */
    @Query("""
        SELECT a
        FROM Appointment a
        JOIN FETCH a.service
        JOIN FETCH a.user
        WHERE a.startsAt >= :start
        AND a.startsAt < :end
    """)
    List<Appointment> findAllWithServiceInPeriod(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
