package com.bumble.pethotel.models.payload.requestModel;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateRoomRequest {
    @NotBlank(message = "Room name is required.")
    private String name;
    @NotBlank(message = "Description is required.")
    private String description;

    @Min(value = 0, message = "Price must be greater than or equal to 0.")
    private double price;

    @NotBlank(message = "Status is required.")
    private String status;
    @NotBlank(message = "Sign is required.")
    private String sign;
    @NotNull(message = "Shop ID is required.")
    private Long shopId;

    @NotNull(message = "Room Type ID is required.")
    private Long roomTypeId;

    @NotNull(message = "Codes list is required.")
    @Size(min = 1, message = "At least one code is required.")
    private List<@NotBlank(message = "Each sign must not be blank.") String> codes;
}
