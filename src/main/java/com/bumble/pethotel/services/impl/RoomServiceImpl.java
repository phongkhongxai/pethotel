package com.bumble.pethotel.services.impl;

import com.bumble.pethotel.models.entity.*;
import com.bumble.pethotel.models.exception.PetApiException;
import com.bumble.pethotel.models.payload.dto.RoomDto;
import com.bumble.pethotel.models.payload.requestModel.CreateRoomRequest;
import com.bumble.pethotel.models.payload.responseModel.RoomsAvailableResponse;
import com.bumble.pethotel.models.payload.responseModel.RoomsResponse;
import com.bumble.pethotel.repositories.RoomRepository;
import com.bumble.pethotel.repositories.RoomTypeRepository;
import com.bumble.pethotel.repositories.ShopRepository;
import com.bumble.pethotel.services.RoomService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@Service

public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Override
    public List<RoomDto> createRoom(CreateRoomRequest createRoomRequest) {
        // Kiểm tra xem shop có tồn tại không
        Optional<Shop> shopOptional = shopRepository.findById(createRoomRequest.getShopId());
        if (shopOptional.isEmpty()) {
            throw new PetApiException(HttpStatus.NOT_FOUND,"Shop not found by id: ."+ createRoomRequest.getShopId());
        }
        RoomType roomType = roomTypeRepository.findById(createRoomRequest.getRoomTypeId())
                .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND,"RoomType not found with id " + createRoomRequest.getRoomTypeId()));


        // Kiểm tra dấu hiệu (sign) có bị trùng nhau trong danh sách không
        Set<String> uniqueCodes = new HashSet<>(createRoomRequest.getCodes());
        if (uniqueCodes.size() != createRoomRequest.getCodes().size()) {
            throw new PetApiException(HttpStatus.BAD_REQUEST,"Duplicate codes in the list are not allowed.");
        }
        List<RoomDto> createdRooms = new ArrayList<>();
        // Tạo mới các phòng
        for (String code : createRoomRequest.getCodes()) {
            if (roomRepository.existsByShopAndCode(shopOptional.get(), code)){
                throw new PetApiException(HttpStatus.BAD_REQUEST,"Duplicate codes in your shop are not allowed.");

            }
            Room newRoom = Room.builder()
                    .name(createRoomRequest.getName())
                    .description(createRoomRequest.getDescription())
                    .price(createRoomRequest.getPrice())
                    .status("available")
                    .sign(createRoomRequest.getSign())
                    .code(code)
                    .shop(shopOptional.get()).roomType(roomType)
                    .build();

            Room savedRoom = roomRepository.save(newRoom);
            RoomDto savedRoomDto = modelMapper.map(savedRoom, RoomDto.class);
            createdRooms.add(savedRoomDto);
        }
        return createdRooms;
    }

    @Override
    public RoomDto getRoomById(Long id) {
        Optional<Room> room = roomRepository.findById(id);
        if(room.isEmpty()){
            throw new PetApiException(HttpStatus.NOT_FOUND, "Room not found with id: "+ id);

        }
        return modelMapper.map(room.get(), RoomDto.class);
    }

    @Override
    public RoomsResponse getAllRooms(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Room> rooms = roomRepository.findAllNotDeleted(pageable);

        // get content for page object
        List<Room> listOfRooms = rooms.getContent();

        List<RoomDto> content = listOfRooms.stream().map(bt -> modelMapper.map(bt, RoomDto.class)).collect(Collectors.toList());

        RoomsResponse templatesResponse = new RoomsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(rooms.getNumber());
        templatesResponse.setPageSize(rooms.getSize());
        templatesResponse.setTotalElements(rooms.getTotalElements());
        templatesResponse.setTotalPages(rooms.getTotalPages());
        templatesResponse.setLast(rooms.isLast());

        return templatesResponse;
    }

    @Override
    public RoomsResponse getRoomByShop(Long shopId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Optional<Shop> shopOptional = shopRepository.findById(shopId);
        if (shopOptional.isEmpty()) {
            throw new PetApiException(HttpStatus.NOT_FOUND,"Shop not found by id: ."+ shopId);
        }
        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Room> rooms = roomRepository.findByShopAndIsDeleteFalse(shopOptional.get(),pageable);


        List<Room> listOfRooms = rooms.getContent();

        List<RoomDto> content = listOfRooms.stream().map(bt -> modelMapper.map(bt, RoomDto.class)).collect(Collectors.toList());

        RoomsResponse templatesResponse = new RoomsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(rooms.getNumber());
        templatesResponse.setPageSize(rooms.getSize());
        templatesResponse.setTotalElements(rooms.getTotalElements());
        templatesResponse.setTotalPages(rooms.getTotalPages());
        templatesResponse.setLast(rooms.isLast());

        return templatesResponse;
    }

    @Override
    public RoomDto updateRoomStatus(Long id, String status) {
        List<String> validStatuses = List.of("available", "occupied", "maintenance", "closed");
        if (!validStatuses.contains(status.toLowerCase())) {
            throw new PetApiException(HttpStatus.BAD_REQUEST, "Invalid status: " + status);
        }
        Optional<Room> roomOptional = roomRepository.findById(id);
        if (roomOptional.isEmpty()) {
            throw new PetApiException(HttpStatus.NOT_FOUND, "Room not found with id: " + id);
        }
        Room room = roomOptional.get();
        room.setStatus(status);
        return modelMapper.map(roomRepository.save(room), RoomDto.class);
    }

    @Override
    public String deleteRoom(Long id) {
        Optional<Room> roomOptional = roomRepository.findById(id);
        if (roomOptional.isEmpty()) {
            throw new PetApiException(HttpStatus.NOT_FOUND, "Room not found with id: " + id);
        }
        Room room = roomOptional.get();
        room.setDelete(true);
        return "Deleted successfully.";
    }

    @Override
    public List<RoomsAvailableResponse> getAvailableRoomsBySignAndAmountOfShop(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND,"Pet not found"));
        List<String> signs = roomRepository.findDistinctSignByShopId(shop.getId());
        List<RoomsAvailableResponse> rooms = new ArrayList<>();
        if (!signs.isEmpty()){
            for(String sign : signs){
                List<Room> availableRooms = roomRepository.findAvailableRoomsBySignAndStatus(sign,"available");
                long amout = roomRepository.countAvailableRoomsBySignAndStatus(sign,"available");
                Room room = availableRooms.get(0);
                RoomsAvailableResponse roomsAvailableResponse = new RoomsAvailableResponse(room.getName(),room.getDescription(),room.getSign(),room.getPrice(),amout);
                rooms.add(roomsAvailableResponse);
            }
        }
        return rooms;
    }

    @Override
    public RoomDto findRandomAvailableRoomBySignAndStatus(String sign) {
        Room randomRoom = roomRepository.findRandomAvailableRoomBySignAndStatus(sign, "available")
                .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND, "No available rooms found"));
        return modelMapper.map(randomRoom, RoomDto.class);
    }




}
