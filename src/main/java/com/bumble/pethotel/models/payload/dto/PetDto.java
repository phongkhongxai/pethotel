package com.bumble.pethotel.models.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetDto {
    private Long id;
    private String name;
    private int age;
    private String breed;
    private String color;
    private int weight;
    private String gender;
    private Long petTypeId;
    private Long userId;
}
