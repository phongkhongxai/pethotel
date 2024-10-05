package com.bumble.pethotel.models.payload.responseModel;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomsAvailableResponse {
    private String name;
    private String description;
    private String sign;
    private double price;
    private Long available;
}
