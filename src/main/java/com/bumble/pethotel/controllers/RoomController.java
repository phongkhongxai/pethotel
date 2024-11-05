package com.bumble.pethotel.controllers;

import com.bumble.pethotel.models.payload.dto.CareServiceDto;
import com.bumble.pethotel.models.payload.dto.RoomDto;
import com.bumble.pethotel.models.payload.requestModel.CareServiceUpdated;
import com.bumble.pethotel.models.payload.requestModel.CreateRoomRequest;
import com.bumble.pethotel.models.payload.responseModel.CareServicesResponse;
import com.bumble.pethotel.models.payload.responseModel.RoomsAvailableResponse;
import com.bumble.pethotel.models.payload.responseModel.RoomsResponse;
import com.bumble.pethotel.services.RoomService;
import com.bumble.pethotel.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createRoom(@Valid @RequestBody CreateRoomRequest createRoomRequest) {
        List<RoomDto> pt = roomService.createRoom(createRoomRequest);
        return new ResponseEntity<>(pt, HttpStatus.CREATED);
    }
    @GetMapping
    public RoomsResponse getAllRooms(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                     @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                     @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                     @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){
        return roomService.getAllRooms(pageNo, pageSize, sortBy, sortDir);
    }
    @GetMapping("/shops/{shopId}")
    public RoomsResponse getRoomsOfShop(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                  @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                  @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                  @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
                                                  @PathVariable("shopId") Long shopId){
        return roomService.getRoomByShop(shopId,pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/available/shops/{shopId}")
    public ResponseEntity<?> getRoomsOfShopAvailable(@PathVariable("shopId") Long shopId){
        List<RoomsAvailableResponse> list = roomService.getAvailableRoomsBySignAndAmountOfShop(shopId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    @GetMapping("/available/shops/random-room-by-sign")
    public ResponseEntity<?> getRandomRoomsOfShopBySign(@RequestParam String sign){
        RoomDto room = roomService.findRandomAvailableRoomBySignAndStatus(sign);
        return new ResponseEntity<>(room, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable("id") Long id) {
        RoomDto roomDto = roomService.getRoomById(id);
        return new ResponseEntity<>(roomDto, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")  // "available", "occupied", "maintenance", "closed"
    public ResponseEntity<?> updateStatusOfRoom(@PathVariable("id") Long id, @RequestParam(value = "status") String status ) {
        RoomDto bt1 = roomService.updateRoomStatus(id, status);
        return new ResponseEntity<>(bt1, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable("id") Long id) {
        String msg = roomService.deleteRoom(id);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}
