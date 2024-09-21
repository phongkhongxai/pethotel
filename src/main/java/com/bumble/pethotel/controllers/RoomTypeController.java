package com.bumble.pethotel.controllers;

import com.bumble.pethotel.models.payload.dto.PetTypeDto;
import com.bumble.pethotel.models.payload.dto.RoomTypeDto;
import com.bumble.pethotel.services.PetTypeService;
import com.bumble.pethotel.services.RoomTypeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/room-types")
public class RoomTypeController {
    @Autowired
    private RoomTypeService roomTypeService;

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createRoomType(@Valid @RequestBody RoomTypeDto roomTypeDto) {
        RoomTypeDto rt = roomTypeService.saveRoomType(roomTypeDto);
        return new ResponseEntity<>(rt, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllRoomType(){
        List<RoomTypeDto> roomTypeDtos = roomTypeService.getAllRoomTypes();
        return new ResponseEntity<>(roomTypeDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomTypeById(@PathVariable("id") Long id) {
        RoomTypeDto roomTypeDto = roomTypeService.getRoomTypeById(id);
        return new ResponseEntity<>(roomTypeDto, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoomType(@PathVariable("id") Long id, @Valid @RequestBody RoomTypeDto roomTypeDto) {
        RoomTypeDto bt1 = roomTypeService.updateRoomType(id, roomTypeDto);
        return new ResponseEntity<>(bt1, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoomType(@PathVariable("id") Long id) {
        String msg = roomTypeService.deleteRoomType(id);
        return new ResponseEntity<>(msg, HttpStatus.NO_CONTENT);
    }
}
