package com.bumble.pethotel.models.payload.requestModel;

import lombok.Data;

@Data
public class ReviewUpdateRequest {
    private int rating;
    private String feedback;
}
