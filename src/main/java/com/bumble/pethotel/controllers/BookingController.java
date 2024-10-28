package com.bumble.pethotel.controllers;

import com.bumble.pethotel.models.payload.dto.BookingDto;
import com.bumble.pethotel.models.payload.responseModel.BookingsResponse;
import com.bumble.pethotel.models.payload.responseModel.RoomsResponse;
import com.bumble.pethotel.services.BookingService;
import com.bumble.pethotel.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingDto bookingDto) {
        BookingDto pt = bookingService.createBooking(bookingDto);
        return new ResponseEntity<>(pt, HttpStatus.CREATED);
    }
    @GetMapping
    public BookingsResponse getAllBookings(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                     @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                     @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                     @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){
        return bookingService.getAllBookings(pageNo, pageSize, sortBy, sortDir);
    }
    @GetMapping("/pending/users/{userId}")
    public BookingsResponse getBookingsOfUserPending(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                        @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                        @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                        @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
                                        @PathVariable("userId") Long userId){
        return bookingService.getAllBookingsOfUser(userId,"PENDING",pageNo, pageSize, sortBy, sortDir);
    }


    @GetMapping("/completed/users/{userId}")
    public BookingsResponse getBookingsOfUserCompleted(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                              @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                              @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                              @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
                                              @PathVariable("userId") Long userId){
        return bookingService.getAllBookingsOfUser(userId,"COMPLETED",pageNo, pageSize, sortBy, sortDir);
    }
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> updateStatusCancelBooking(@PathVariable("id")Long id) {
        String msg = bookingService.updateStatusBooking(id,"CANCELLED");
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @GetMapping("/shops/{shopId}")
    public BookingsResponse getBookingsOfShop(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                              @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                              @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                              @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
                                              @PathVariable("shopId") Long shopId){
        return bookingService.getAllBookingsOfShop(shopId,pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable("id") Long id) {
        BookingDto bookingDto = bookingService.getBookingById(id);
        return new ResponseEntity<>(bookingDto, HttpStatus.OK);
    }
}
