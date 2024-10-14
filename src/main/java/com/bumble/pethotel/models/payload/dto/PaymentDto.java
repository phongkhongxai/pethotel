package com.bumble.pethotel.models.payload.dto;

import com.bumble.pethotel.models.entity.Booking;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long id;
    private String method;
    private LocalDateTime date ;
    private double amount;
    private String status;
    private String qrCodeUrl;
    private Long orderCode;
    private Long bookingId;
}
