package com.bumble.pethotel.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="booking_id")
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private LocalDate startDate ;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private double totalPrice;

    @Column
    private double discount;

    @Column(nullable = false)
    private LocalDateTime dateBooking;

    @Column(nullable = false)
    private boolean isDelete = false;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "BookingService",
            joinColumns = {@JoinColumn(name = "booking_id", referencedColumnName = "booking_id")},
            inverseJoinColumns = {@JoinColumn(name = "service_id", referencedColumnName = "service_id")}
    )
    private Set<CareService> careServices;
}
