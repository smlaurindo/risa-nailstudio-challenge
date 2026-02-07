package com.smlaurindo.risanailstudio.adapter.outbound.persitence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerJpaEntity {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private UserJpaEntity user;

    @Column(name = "name")
    private String name;

    @Column(name = "photo")
    private String photo;

    public CustomerJpaEntity(String id) {
        this.id = id;
    }
}
