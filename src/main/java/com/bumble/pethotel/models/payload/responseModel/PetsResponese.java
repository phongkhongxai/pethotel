package com.bumble.pethotel.models.payload.responseModel;

import com.bumble.pethotel.models.payload.dto.PetDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PetsResponese {
    private List<PetDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
