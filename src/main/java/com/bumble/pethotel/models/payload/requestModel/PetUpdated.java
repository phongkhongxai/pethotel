package com.bumble.pethotel.models.payload.requestModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetUpdated {
    private String name;
    private double age;
    private String breed;
    private String color;
    private double weight;
    private String gender;
    private Long petTypeId;
}
