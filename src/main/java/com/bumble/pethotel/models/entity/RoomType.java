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
@Table(name = "room_types")
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="roomtype_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String note;

    @Column(nullable = false)
    private boolean isDelete = false;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Room> rooms;
}
