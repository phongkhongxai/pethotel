package com.bumble.pethotel.models.payload.responseModel;

import com.bumble.pethotel.models.payload.dto.ReviewDto;
import com.bumble.pethotel.models.payload.dto.RoomDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewsResponse {
    private List<ReviewDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
