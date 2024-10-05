package com.bumble.pethotel.models.payload.requestModel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CareServiceUpdated {
    private String name;
    private String description;
    private String status;
    @Positive(message = "Price must be a positive number")
    private double price;
}
