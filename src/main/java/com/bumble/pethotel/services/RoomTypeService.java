package com.bumble.pethotel.services;

import com.bumble.pethotel.models.payload.dto.PetTypeDto;
import com.bumble.pethotel.models.payload.dto.RoomTypeDto;

import java.util.List;

public interface RoomTypeService {
    RoomTypeDto saveRoomType(RoomTypeDto roomTypeDto);
    RoomTypeDto getRoomTypeById(Long id);
    List<RoomTypeDto> getAllRoomTypes();
    RoomTypeDto updateRoomType(Long id, RoomTypeDto roomTypeDto);
    String deleteRoomType(Long id);
}
