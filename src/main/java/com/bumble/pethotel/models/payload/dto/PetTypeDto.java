package com.bumble.pethotel.models.payload.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetTypeDto {
    private Long id;
    @NotEmpty(message = "Name should not be empty!")
    private String name;
}
