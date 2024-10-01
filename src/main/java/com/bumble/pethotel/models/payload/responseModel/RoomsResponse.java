package com.bumble.pethotel.models.payload.responseModel;

import com.bumble.pethotel.models.payload.dto.PetDto;
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
public class RoomsResponse {
    private List<RoomDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
