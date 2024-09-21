package com.bumble.pethotel.services.impl;


import com.bumble.pethotel.models.entity.PetType;
import com.bumble.pethotel.models.entity.RoomType;
import com.bumble.pethotel.models.exception.PetApiException;
import com.bumble.pethotel.models.payload.dto.PetTypeDto;
import com.bumble.pethotel.models.payload.dto.RoomTypeDto;
import com.bumble.pethotel.repositories.RoomTypeRepository;
import com.bumble.pethotel.services.RoomTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Override
    public RoomTypeDto saveRoomType(RoomTypeDto roomTypeDto) {
        RoomType roomType =modelMapper.map(roomTypeDto, RoomType.class);
        roomType.setDelete(false);
        return modelMapper.map(roomTypeRepository.save(roomType), RoomTypeDto.class);
    }

    @Override
    public RoomTypeDto getRoomTypeById(Long id) {
        Optional<RoomType>  roomType = roomTypeRepository.findById(id);
        if(roomType.isEmpty()){
            throw new PetApiException(HttpStatus.NOT_FOUND,"Room Type not found with ID: "+id);
        }
        return modelMapper.map(roomType.get(),RoomTypeDto.class);
    }

    @Override
    public List<RoomTypeDto> getAllRoomTypes() {
        return roomTypeRepository.findAllNotDeleted().stream().map(bt -> modelMapper.map(bt, RoomTypeDto.class)).toList();

    }

    @Override
    public RoomTypeDto updateRoomType(Long id, RoomTypeDto roomTypeDto) {
        Optional<RoomType> roomType = roomTypeRepository.findById(id);
        if(roomType.isEmpty()){
            throw new PetApiException(HttpStatus.NOT_FOUND,"Room Type not found with ID: "+id);
        }
        RoomType existingRoomType = roomType.get();
        existingRoomType.setName(roomTypeDto.getName() != null ? roomTypeDto.getName() : existingRoomType.getName());
        existingRoomType.setNote(roomTypeDto.getNote() != null ? roomTypeDto.getNote() : existingRoomType.getNote());
        RoomType updatedRoomType = roomTypeRepository.save(existingRoomType);
        return modelMapper.map(updatedRoomType, RoomTypeDto.class);
    }
    @Override
    public String deleteRoomType(Long id) {
        Optional<RoomType> roomType = roomTypeRepository.findById(id);
        if(roomType.isEmpty()){
            throw new PetApiException(HttpStatus.NOT_FOUND,"Room Type not found with ID: "+id);
        }
        RoomType existingRoomType = roomType.get();
        existingRoomType.setDelete(true);
        roomTypeRepository.save(existingRoomType);
        return "Deleted successfully.";
    }
}
