package com.stayease.controller;

import com.stayease.dto.request.BookingRequest;
import com.stayease.dto.response.ApiResponse;
import com.stayease.dto.response.BookingResponse;
import com.stayease.security.SecurityUtils;
import com.stayease.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(@Valid @RequestBody BookingRequest request) {
        String email = SecurityUtils.getCurrentUserEmail()
                .orElseThrow(() -> new AccessDeniedException("User must be authenticated to create a reservation"));

        BookingResponse response = bookingService.createBooking(request, email);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Booking confirmed successfully!", response));
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getMyBookings() {
        String email = SecurityUtils.getCurrentUserEmail()
                .orElseThrow(() -> new AccessDeniedException("User must be authenticated to view reservations"));

        List<BookingResponse> bookings = bookingService.getMyBookings(email);
        return ResponseEntity.ok(ApiResponse.ok(bookings));
    }

    @GetMapping("/reference/{ref}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingByReference(@PathVariable String ref) {
        String email = SecurityUtils.getCurrentUserEmail()
                .orElseThrow(() -> new AccessDeniedException("User must be authenticated to view reservations"));

        BookingResponse booking = bookingService.getBookingByReference(ref, email);
        return ResponseEntity.ok(ApiResponse.ok(booking));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingById(@PathVariable Long id) {
        String email = SecurityUtils.getCurrentUserEmail()
                .orElseThrow(() -> new AccessDeniedException("User must be authenticated to view reservations"));

        BookingResponse booking = bookingService.getBookingById(id, email);
        return ResponseEntity.ok(ApiResponse.ok(booking));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(@PathVariable Long id) {
        String email = SecurityUtils.getCurrentUserEmail()
                .orElseThrow(() -> new AccessDeniedException("User must be authenticated to cancel reservations"));

        BookingResponse cancelled = bookingService.cancelBooking(id, email);
        return ResponseEntity.ok(ApiResponse.ok("Reservation cancelled successfully", cancelled));
    }
}
