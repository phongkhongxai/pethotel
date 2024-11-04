package com.bumble.pethotel.services.impl;

import com.bumble.pethotel.models.entity.*;
import com.bumble.pethotel.models.exception.PetApiException;
import com.bumble.pethotel.models.exception.ResourceNotFoundException;
import com.bumble.pethotel.models.payload.dto.BookingDto;
import com.bumble.pethotel.models.payload.responseModel.BookingsResponse;
import com.bumble.pethotel.repositories.*;
import com.bumble.pethotel.services.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CareServiceRepository careServiceRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ShopRepository shopRepository;

    @Override
    public BookingDto createBooking(BookingDto bookingDto) {
        Pet pet = petRepository.findById(bookingDto.getPetId())
                .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND,"Pet not found"));

        User user = userRepository.findById(bookingDto.getUserId())
                .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND,"User not found"));
        Booking.BookingBuilder bookingBuilder = Booking.builder()
                .startDate(bookingDto.getStartDate())
                //.endDate(bookingDto.getEndDate())
                .status("PENDING")
                .dateBooking(LocalDateTime.now())
                .pet(pet)
                .user(user)
                .isDelete(false);
        if (bookingDto.getNote() != null) {
            bookingBuilder.note(bookingDto.getNote());
        }
        double totalPrice = 0;
        boolean hasRoom = false;

        Long shopId = null;

        if (bookingDto.getRoomId() != null) {
            Room room = roomRepository.findById(bookingDto.getRoomId())
                    .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND,"Room not found"));
            long daysBetween = ChronoUnit.DAYS.between(bookingDto.getStartDate(), bookingDto.getEndDate());

            if (daysBetween <= 0) {
                throw new PetApiException(HttpStatus.BAD_REQUEST,"End date must be after start date");
            }
            shopId = room.getShop().getId();
            double roomPrice = room.getPrice() * daysBetween;
            totalPrice += roomPrice;  // Cộng giá phòng vào tổng giá
            bookingBuilder.room(room);  // Gán Room vào Booking
            bookingBuilder.endDate(bookingDto.getEndDate());
            hasRoom = true;
        }
        boolean hasService = false;
        if (bookingDto.getServiceIds() != null && !bookingDto.getServiceIds().isEmpty()) {
            Set<CareService> careServices = careServiceRepository.findAllById(bookingDto.getServiceIds())
                    .stream()
                    .collect(Collectors.toSet());
            bookingBuilder.careServices(careServices);
            for (CareService service : careServices) {
                totalPrice += service.getPrice();

                // Kiểm tra shopId của các Service
                if (shopId != null && !service.getShop().getId().equals(shopId)) {
                    throw new PetApiException(HttpStatus.BAD_REQUEST, "Room and Service must belong to the same shop");
                }
                // Nếu chưa có shopId (chỉ có dịch vụ mà không có room), gán shopId của service
                if (shopId == null) {
                    shopId = service.getShop().getId();
                }
            }
            hasService = true;
        }
        String type;
        if (hasRoom && hasService) {
            type = "Both";
        } else if (hasRoom) {
            type = "Room";
        } else if (hasService) {
            type = "Service";
        } else {
            throw new PetApiException(HttpStatus.BAD_REQUEST,"Must book either a room or a service.");
        }

        double discountRate = user.isPremium() ? 0.05 : 0.10;
        double discount = totalPrice * discountRate;
        totalPrice += discount;
        Booking booking = bookingBuilder.type(type).totalPrice(totalPrice).build();

        Booking savedBooking = bookingRepository.save(booking);

        return modelMapper.map(savedBooking, BookingDto.class);
    }

    @Override
    public BookingDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND,"Booking not found"));
        return modelMapper.map(booking, BookingDto.class);
    }

    @Override
    public BookingsResponse getAllBookings(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Booking> bookings = bookingRepository.findAllNotDeleted(pageable);


        List<Booking> listOfBookings = bookings.getContent();

        List<BookingDto> content = listOfBookings.stream().map(bt -> modelMapper.map(bt, BookingDto.class)).collect(Collectors.toList());

        BookingsResponse templatesResponse = new BookingsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(bookings.getNumber());
        templatesResponse.setPageSize(bookings.getSize());
        templatesResponse.setTotalElements(bookings.getTotalElements());
        templatesResponse.setTotalPages(bookings.getTotalPages());
        templatesResponse.setLast(bookings.isLast());

        return templatesResponse;
    }

    @Override
    public BookingsResponse getAllBookingsOfUser(Long userId,String status, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new PetApiException(HttpStatus.NOT_FOUND,"User not found by id: ."+ userId);
        }
        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Booking> bookings = bookingRepository.findByUserAndStatusAndIsDeleteFalse(userOptional.get(),status,pageable);


        List<Booking> listOfBookings = bookings.getContent();

        List<BookingDto> content = listOfBookings.stream().map(bt -> modelMapper.map(bt, BookingDto.class)).collect(Collectors.toList());

        BookingsResponse templatesResponse = new BookingsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(bookings.getNumber());
        templatesResponse.setPageSize(bookings.getSize());
        templatesResponse.setTotalElements(bookings.getTotalElements());
        templatesResponse.setTotalPages(bookings.getTotalPages());
        templatesResponse.setLast(bookings.isLast());

        return templatesResponse;
    }

    @Override
    public BookingsResponse getAllBookingsOfShop(Long shopId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Optional<Shop> shopOptional = shopRepository.findById(shopId);
        if (shopOptional.isEmpty()) {
            throw new PetApiException(HttpStatus.NOT_FOUND,"Shop not found by id: ."+ shopId);
        }
        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Booking> bookings = bookingRepository.findByShopIdAndIsDeleteFalse(shopOptional.get().getId(),pageable);


        List<Booking> listOfBookings = bookings.getContent();

        List<BookingDto> content = listOfBookings.stream().map(bt -> modelMapper.map(bt, BookingDto.class)).collect(Collectors.toList());

        BookingsResponse templatesResponse = new BookingsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(bookings.getNumber());
        templatesResponse.setPageSize(bookings.getSize());
        templatesResponse.setTotalElements(bookings.getTotalElements());
        templatesResponse.setTotalPages(bookings.getTotalPages());
        templatesResponse.setLast(bookings.isLast());

        return templatesResponse;
    }

    @Override
    public String updateStatusBooking(Long id,String status) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isEmpty()) {
            throw new PetApiException(HttpStatus.NOT_FOUND, "Booking not found with id: " + id);
        }
        Booking booking1 = booking.get();
        booking1.setStatus(status);
        bookingRepository.save(booking1);
        return "Updated status booking successfully";
    }

    @Scheduled(cron = "0 0 0 * * ?")  // Chạy vào 0:00 mỗi ngày
    public void updateRoomStatusDaily() {
        LocalDate today = LocalDate.now();

        List<Booking> expiredBookings = bookingRepository.findByEndDateBeforeAndStatus(today, "COMPLETED");

        for (Booking booking : expiredBookings) {
            Room room = booking.getRoom();
            if (room != null && room.getStatus().equals("occupied")) {
                room.setStatus("available");
                roomRepository.save(room);  // Cập nhật trạng thái Room trong database
            }
        }
    }
}
