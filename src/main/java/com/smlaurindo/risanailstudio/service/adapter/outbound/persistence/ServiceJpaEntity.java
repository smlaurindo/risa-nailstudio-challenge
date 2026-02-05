package com.smlaurindo.risanailstudio.service.adapter.outbound.persistence;

import com.smlaurindo.risanailstudio.service.application.domain.Service;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "services")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "duration_minutes", nullable = false)
    private String durationMinutes;

    @Column(name = "price_cents", nullable = false)
    private Integer priceCents;

    @Column(name = "icon", nullable = false)
    private String icon;

    public Service toDomain() {
        return new Service(id, name, durationMinutes, priceCents, icon);
    }
}
