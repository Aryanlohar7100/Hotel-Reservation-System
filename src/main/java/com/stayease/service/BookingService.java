package com.stayease.service;

import com.stayease.dto.request.BookingRequest;
import com.stayease.dto.response.BookingResponse;
import com.stayease.entity.BookingStatus;
import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request, String userEmail);
    List<BookingResponse> getMyBookings(String userEmail);
    BookingResponse getBookingByReference(String reference, String userEmail);
    BookingResponse getBookingById(Long id, String userEmail);
    BookingResponse cancelBooking(Long bookingId, String userEmail);
    List<BookingResponse> getAllBookingsAdmin();
    BookingResponse updateBookingStatus(Long bookingId, BookingStatus newStatus);
}
