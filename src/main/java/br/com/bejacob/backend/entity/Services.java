package br.com.bejacob.backend.entity;

import br.com.bejacob.backend.dto.service.ServiceRequest;
import br.com.bejacob.backend.dto.service.UpdateServiceRequest;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity(name = "Service")
@Table(name = "service")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_service")
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Boolean active = true;

    public Services(Long id) {
        this.id = id;
    }


    public Services(ServiceRequest request) {
        this.name = request.name();
        this.description = request.description();
        this.durationMinutes = request.durationMinutes();
        this.price = request.price();
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }


    public void updateDetails(UpdateServiceRequest request) {
        if (request.name() != null) this.name = request.name();
        if (request.description() != null) this.description = request.description();
        if (request.durationMinutes() != null) this.durationMinutes = request.durationMinutes();
        if (request.price() != null) this.price = request.price();
        if (request.active() != null) this.active = request.active();
    }
}