package com.bumble.pethotel.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="payment_id")
    private Long id;
    @Column(nullable = false)
    private String method;
    @Column(nullable = false)
    private LocalDateTime date ;
    @Column(nullable = false)
    private boolean isDelete = false;
    @Column(nullable = false)
    private double amount;
    @Column(nullable = false)
    private String status;
    @Column
    private String qrCodeUrl;
    @Column
    private Long userId;
    @Column
    private Long orderCode;
    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
}
