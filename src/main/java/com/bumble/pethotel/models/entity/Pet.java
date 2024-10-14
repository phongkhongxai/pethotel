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
@Table(name = "pets")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pet_id")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private double age;
    @Column(nullable = false)
    private String breed;
    @Column(nullable = false)
    private String color;
    @Column(nullable = false)
    private double weight;
    @Column(nullable = false)
    private String gender;
    @ManyToOne
    @JoinColumn(name = "pettype_id", nullable = false)
    private PetType petType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Booking> bookings;

    @Column(nullable = false)
    private boolean isDelete = false;
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ImageFile> imageFile;
}
