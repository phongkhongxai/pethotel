package com.bumble.pethotel.models.payload.dto;

import com.bumble.pethotel.models.entity.Shop;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CareServiceDto {
    private Long id;
    private String name;
    private String description;
    private String status;
    private double price;
    private Long shopId;
}
