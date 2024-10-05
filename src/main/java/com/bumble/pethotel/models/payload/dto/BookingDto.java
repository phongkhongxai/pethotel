package com.bumble.pethotel.models.payload.dto;

import com.bumble.pethotel.models.entity.Payment;
import com.bumble.pethotel.models.entity.Pet;
import com.bumble.pethotel.models.entity.Room;
import com.bumble.pethotel.models.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;

    private String type;

    @NotNull(message = "Start date is required")
    @Future(message = "Start date must be in the future")
    private LocalDate startDate;

    @Future(message = "Start date must be in the future")
    private LocalDate endDate;
    private String note;
    private String status;

    private double totalPrice;

    private LocalDateTime dateBooking;

    private Long roomId;

    @NotNull(message = "Pet ID is required")
    private Long petId;

    @NotNull(message = "User ID is required")
    private Long userId;

    private Set<Long> serviceIds;

    private Set<CareServiceDto> careServices;

}
