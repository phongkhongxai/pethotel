package com.bumble.pethotel.models.payload.dto;

import com.bumble.pethotel.models.entity.Shop;
import com.bumble.pethotel.models.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
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
public class ReviewDto {
    private Long id;
    private int rating;
    private String feedback;
    private Long userId;
    private Long shopId;
}
