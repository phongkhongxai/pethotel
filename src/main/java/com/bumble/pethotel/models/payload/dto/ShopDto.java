package com.bumble.pethotel.models.payload.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShopDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be between 10 to 15 digits and can include a leading '+'")
    private String phone;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Bank name is required")
    private String bankName;

    @NotBlank(message = "Account number is required")
    @Pattern(regexp = "^[0-9]{10,20}$", message = "Account number must be between 10 to 20 digits")
    private String accountNumber;

    @NotNull(message = "User ID is required")
    private Long userId;
    private Set<CareServiceDto> services;
    private Set<ImageFileDto> imageFiles;
}
