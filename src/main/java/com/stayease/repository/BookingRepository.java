package com.stayease.repository;

import com.stayease.entity.Booking;
import com.stayease.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b LEFT JOIN FETCH b.user LEFT JOIN FETCH b.hotel LEFT JOIN FETCH b.payment WHERE b.bookingReference = :bookingReference")
    Optional<Booking> findByBookingReference(@Param("bookingReference") String bookingReference);

    @Query("SELECT b FROM Booking b LEFT JOIN FETCH b.user LEFT JOIN FETCH b.hotel LEFT JOIN FETCH b.payment WHERE b.user.id = :userId ORDER BY b.createdAt DESC")
    List<Booking> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    @Query("SELECT b FROM Booking b LEFT JOIN FETCH b.user LEFT JOIN FETCH b.hotel LEFT JOIN FETCH b.payment ORDER BY b.createdAt DESC")
    List<Booking> findAllByOrderByCreatedAtDesc();

    List<Booking> findTop10ByOrderByCreatedAtDesc();

    long countByBookingStatus(BookingStatus status);

    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Booking b WHERE b.bookingStatus <> com.stayease.entity.BookingStatus.CANCELLED")
    BigDecimal sumTotalRevenue();

    @Query("SELECT COUNT(DISTINCT b.id) FROM Booking b " +
           "WHERE b.bookingStatus IN (com.stayease.entity.BookingStatus.CONFIRMED, com.stayease.entity.BookingStatus.CHECKED_IN) " +
           "AND b.checkInDate <= :today AND b.checkOutDate > :today")
    long countCurrentlyActiveOccupancies(@Param("today") LocalDate today);
}
