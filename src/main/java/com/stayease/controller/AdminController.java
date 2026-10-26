package com.stayease.controller;

import com.stayease.dto.response.AdminStatsResponse;
import com.stayease.dto.response.ApiResponse;
import com.stayease.dto.response.BookingResponse;
import com.stayease.entity.BookingStatus;
import com.stayease.service.AnalyticsService;
import com.stayease.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AnalyticsService analyticsService;
    private final BookingService bookingService;

    public AdminController(AnalyticsService analyticsService, BookingService bookingService) {
        this.analyticsService = analyticsService;
        this.bookingService = bookingService;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<AdminStatsResponse>> getStats() {
        AdminStatsResponse stats = analyticsService.getDashboardStats();
        return ResponseEntity.ok(ApiResponse.ok(stats));
    }

    @GetMapping("/recent-bookings")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getRecentBookings() {
        List<BookingResponse> recent = analyticsService.getRecentBookings();
        return ResponseEntity.ok(ApiResponse.ok(recent));
    }

    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getAllBookings() {
        List<BookingResponse> allBookings = bookingService.getAllBookingsAdmin();
        return ResponseEntity.ok(ApiResponse.ok(allBookings));
    }

    @PutMapping("/bookings/{id}/status")
    public ResponseEntity<ApiResponse<BookingResponse>> updateBookingStatus(
            @PathVariable Long id,
            @RequestParam BookingStatus status) {
        BookingResponse updated = bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok(ApiResponse.ok("Booking status updated to " + status, updated));
    }

    @GetMapping("/customers")
    public ResponseEntity<ApiResponse<List<com.stayease.dto.response.CustomerSummaryResponse>>> getAllCustomers() {
        List<com.stayease.dto.response.CustomerSummaryResponse> customers = analyticsService.getAllCustomers();
        return ResponseEntity.ok(ApiResponse.ok(customers));
    }

    @PutMapping("/customers/{id}/toggle-status")
    public ResponseEntity<ApiResponse<com.stayease.dto.response.CustomerSummaryResponse>> toggleCustomerStatus(@PathVariable Long id) {
        com.stayease.dto.response.CustomerSummaryResponse updated = analyticsService.toggleCustomerStatus(id);
        return ResponseEntity.ok(ApiResponse.ok("Customer status updated", updated));
    }
}
