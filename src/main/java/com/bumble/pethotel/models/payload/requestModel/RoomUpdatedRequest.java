package com.bumble.pethotel.models.payload.requestModel;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoomUpdatedRequest {
    private String name;
    private String description;
    @Min(value = 0, message = "Price must be greater than or equal to 0.")
    private double price;
}
