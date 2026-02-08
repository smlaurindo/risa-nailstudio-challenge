package com.smlaurindo.risanailstudio.adapter.outbound.persitence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admins")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminJpaEntity {
    @Id
    @Column(name = "id")
    private String id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private UserJpaEntity user;

    @Column(name = "name")
    private String name;

    public AdminJpaEntity(String id) {
        this.id = id;
    }
}
