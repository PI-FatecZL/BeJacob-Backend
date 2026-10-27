package br.com.bejacob.backend.entity;

import br.com.bejacob.backend.entity.enums.AppointmentStatus;
import br.com.bejacob.backend.entity.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "Appointment")
@Table(name = "appointment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Services service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;

    @Column(name = "ends_at", nullable = false)
    private LocalDateTime endsAt;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private AppointmentStatus status;

 
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 10)
    private PaymentMethod paymentMethod;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Appointment(Long id) {
        this.id = id;
    }


    // agendamento pra  cliente
    public Appointment(User user, Services service, LocalDateTime startsAt, LocalDateTime endsAt, String notes, PaymentMethod paymentMethod) {
        this(user, service, startsAt, endsAt, notes, AppointmentStatus.SCHEDULED, paymentMethod);
    }

    // agendamento pra admin
    public Appointment(User user, Services service, LocalDateTime startsAt, LocalDateTime endsAt, String notes, AppointmentStatus status, PaymentMethod paymentMethod) {
        this.user = user;
        this.service = service;
        this.price = service.getPrice();
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.notes = notes;
        this.status = status != null ? status : AppointmentStatus.SCHEDULED;
        this.paymentMethod = paymentMethod;
    }


    public void cancel() {
        this.status = AppointmentStatus.CANCELED;
    }

    public void updateStatus(AppointmentStatus newStatus) {
        if (newStatus != null) {
            this.status = newStatus;
        }
    }

    public void updateDetails(Services newService, LocalDateTime newStartsAt, LocalDateTime newEndsAt, String newNotes, AppointmentStatus newstatus, PaymentMethod newPaymentMethod) {
        if (newService != null && !newService.getId().equals(this.service.getId())) {
            this.service = newService;
            this.price = newService.getPrice();
        }
        if (newStartsAt != null) this.startsAt = newStartsAt;
        if (newEndsAt != null) this.endsAt = newEndsAt;
        if (newNotes != null) this.notes = newNotes;
        if (newstatus != null) this.status = newstatus;
        if (newPaymentMethod != null) this.paymentMethod = newPaymentMethod;
    }
}