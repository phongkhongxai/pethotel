package com.bumble.pethotel.services;

import com.bumble.pethotel.models.payload.dto.BookingDto;
import com.bumble.pethotel.models.payload.responseModel.BookingsResponse;

public interface BookingService {
    BookingDto createBooking(BookingDto bookingDto);
    BookingDto getBookingById(Long id);
    BookingsResponse getAllBookings(int pageNo, int pageSize, String sortBy, String sortDir);
    BookingsResponse getAllBookingsOfUser(Long userId,int pageNo, int pageSize, String sortBy, String sortDir);

}
