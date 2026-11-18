package br.com.bejacob.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "appointment_google_event")
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentGoogleEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    @Column(name = "google_event_id")
    private String googleEventId;

    public AppointmentGoogleEvent(Appointment appointment, String googleEventId) {
        this.appointment = appointment;
        this.googleEventId = googleEventId;
    }
}
