package com.bumble.pethotel.models.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShopDto {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String description;
    private String bankName;
    private String accountNumber;
    private Long userId;
}
