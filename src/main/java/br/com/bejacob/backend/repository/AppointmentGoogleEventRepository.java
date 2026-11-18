package br.com.bejacob.backend.repository;

import br.com.bejacob.backend.entity.AppointmentGoogleEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppointmentGoogleEventRepository extends JpaRepository<AppointmentGoogleEvent, Long> {
    Optional<AppointmentGoogleEvent> findByAppointmentId(Long appointmentId);
}
