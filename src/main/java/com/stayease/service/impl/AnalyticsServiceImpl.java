package com.stayease.service.impl;

import com.stayease.dto.response.AdminStatsResponse;
import com.stayease.dto.response.BookingResponse;
import com.stayease.entity.Role;
import com.stayease.entity.User;
import com.stayease.repository.BookingRepository;
import com.stayease.repository.HotelRepository;
import com.stayease.repository.RoomRepository;
import com.stayease.repository.UserRepository;
import com.stayease.service.AnalyticsService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public AnalyticsServiceImpl(BookingRepository bookingRepository,
                                HotelRepository hotelRepository,
                                RoomRepository roomRepository,
                                UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AdminStatsResponse getDashboardStats() {
        long totalHotels = hotelRepository.count();
        long totalRooms = roomRepository.count();
        long totalBookings = bookingRepository.count();
        long totalCustomers = userRepository.countByRole(Role.ROLE_CUSTOMER);
        BigDecimal totalRevenue = bookingRepository.sumTotalRevenue();
        long activeOccupancies = bookingRepository.countCurrentlyActiveOccupancies(LocalDate.now());

        double occupancyRate = 0.0;
        if (totalRooms > 0) {
            occupancyRate = BigDecimal.valueOf((double) activeOccupancies / totalRooms * 100.0)
                    .setScale(1, RoundingMode.HALF_UP)
                    .doubleValue();
        }

        return new AdminStatsResponse(
                totalHotels,
                totalRooms,
                totalBookings,
                totalCustomers,
                totalRevenue != null ? totalRevenue : BigDecimal.ZERO,
                activeOccupancies,
                occupancyRate
        );
    }

    @Override
    public List<BookingResponse> getRecentBookings() {
        return bookingRepository.findTop10ByOrderByCreatedAtDesc()
                .stream()
                .map(BookingResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<com.stayease.dto.response.CustomerSummaryResponse> getAllCustomers() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.ROLE_CUSTOMER)
                .map(u -> {
                    long bookingsCount = bookingRepository.findByUserIdOrderByCreatedAtDesc(u.getId()).size();
                    return com.stayease.dto.response.CustomerSummaryResponse.fromEntity(u, bookingsCount);
                })
                .collect(Collectors.toList());
    }

    @Override
    public com.stayease.dto.response.CustomerSummaryResponse toggleCustomerStatus(Long customerId) {
        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new com.stayease.exception.ResourceNotFoundException("Customer not found with ID: " + customerId));
        user.setActive(!user.isActive());
        User saved = userRepository.save(user);
        long bookingsCount = bookingRepository.findByUserIdOrderByCreatedAtDesc(saved.getId()).size();
        return com.stayease.dto.response.CustomerSummaryResponse.fromEntity(saved, bookingsCount);
    }
}
