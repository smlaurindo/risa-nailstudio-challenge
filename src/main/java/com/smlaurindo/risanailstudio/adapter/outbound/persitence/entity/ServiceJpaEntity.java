package com.smlaurindo.risanailstudio.adapter.outbound.persitence.entity;

import com.smlaurindo.risanailstudio.application.domain.Service;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @Column(name = "id")
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    @Column(name = "price_cents", nullable = false)
    private int priceCents;

    @Column(name = "icon", nullable = false)
    private String icon;

    public ServiceJpaEntity(String id) {
        this.id = id;
    }

    public Service toDomain() {
        return new Service(id, name, durationMinutes, priceCents, icon);
    }
}
