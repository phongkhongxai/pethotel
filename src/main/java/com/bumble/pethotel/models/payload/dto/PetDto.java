package com.bumble.pethotel.models.payload.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @DecimalMin(value = "0.0", message = "Age must be a positive number or zero")
    @DecimalMax(value = "30.0", message = "Age cannot exceed 30 years")
    private double age;

    @NotBlank(message = "Breed is required")
    private String breed;

    @NotBlank(message = "Color is required")
    private String color;

    @Positive(message = "Weight must be a positive number")
    private double weight;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotNull(message = "Pet Type ID is required")
    private Long petTypeId;

    @NotNull(message = "User ID is required")
    private Long userId;
    private Set<ImageFileDto> imageFile;
    private List<MultipartFile> files;


}
