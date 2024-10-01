package com.bumble.pethotel.models.payload.requestModel;

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
    private double price;
}
