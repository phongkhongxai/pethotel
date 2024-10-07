package com.bumble.pethotel.models.payload.dto;

import com.bumble.pethotel.models.entity.Shop;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CareServiceDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Status is required")
    private String status;
    @NotBlank(message = "Type is required")
    @Pattern(regexp = "^(spa|health)$", message = "Type must be either 'spa' or 'health'")
    private String type;
    @PositiveOrZero(message = "Price must be zero or positive")
    private double price;
    @NotNull(message = "Shop ID is required")
    private Long shopId;
}
