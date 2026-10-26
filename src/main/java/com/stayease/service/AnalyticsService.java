package com.stayease.service;

import com.stayease.dto.response.AdminStatsResponse;
import com.stayease.dto.response.BookingResponse;
import java.util.List;

public interface AnalyticsService {
    AdminStatsResponse getDashboardStats();
    List<BookingResponse> getRecentBookings();
    List<com.stayease.dto.response.CustomerSummaryResponse> getAllCustomers();
    com.stayease.dto.response.CustomerSummaryResponse toggleCustomerStatus(Long customerId);
}
