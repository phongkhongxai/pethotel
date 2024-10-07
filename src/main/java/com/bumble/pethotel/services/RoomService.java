package com.bumble.pethotel.services;

import com.bumble.pethotel.models.payload.dto.CareServiceDto;
import com.bumble.pethotel.models.payload.dto.RoomDto;
import com.bumble.pethotel.models.payload.requestModel.CareServiceUpdated;
import com.bumble.pethotel.models.payload.requestModel.CreateRoomRequest;
import com.bumble.pethotel.models.payload.requestModel.RoomUpdatedRequest;
import com.bumble.pethotel.models.payload.responseModel.CareServicesResponse;
import com.bumble.pethotel.models.payload.responseModel.RoomsAvailableResponse;
import com.bumble.pethotel.models.payload.responseModel.RoomsResponse;

import java.util.List;

public interface RoomService {
    List<RoomDto> createRoom(CreateRoomRequest createRoomRequest);
    RoomDto getRoomById(Long id);
    RoomsResponse getAllRooms(int pageNo, int pageSize, String sortBy, String sortDir);
    RoomsResponse getRoomByShop(Long shopId, int pageNo, int pageSize, String sortBy, String sortDir);
    RoomDto updateRoomStatus(Long id, String status);
    String deleteRoom(Long id);
    List<RoomsAvailableResponse> getAvailableRoomsBySignAndAmountOfShop(Long shopId);
    RoomDto findRandomAvailableRoomBySignAndStatus(String sign);
}
