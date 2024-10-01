package com.bumble.pethotel.models.payload.dto;

import com.bumble.pethotel.models.entity.RoomType;
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
public class RoomDto {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String status;
    private String sign;
    private String code;
    private Long shopId;
    private Long roomTypeId;
}
