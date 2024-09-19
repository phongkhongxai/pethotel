package com.bumble.pethotel.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "pet_types")
public class PetType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pettype_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean isDelete = false;

    @OneToMany(mappedBy = "petType", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Pet> pets;
}
