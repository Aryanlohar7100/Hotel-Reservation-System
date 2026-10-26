package com.stayease.repository;

import com.stayease.entity.Room;
import com.stayease.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT r FROM Room r WHERE r.hotel.id = :hotelId AND r.active = true")
    List<Room> findByHotelIdAndActiveTrue(@Param("hotelId") Long hotelId);

    @Query("SELECT r FROM Room r WHERE r.hotel.id = :hotelId")
    List<Room> findByHotelId(@Param("hotelId") Long hotelId);

    @Query("SELECT r FROM Room r " +
           "WHERE r.hotel.id = :hotelId " +
           "AND r.active = true " +
           "AND (:roomType IS NULL OR r.roomType = :roomType) " +
           "AND (:minCapacity IS NULL OR (r.capacityAdults + r.capacityChildren) >= :minCapacity) " +
           "AND r.id NOT IN (" +
           "   SELECT bi.room.id FROM BookingItem bi " +
           "   WHERE bi.booking.bookingStatus <> com.stayease.entity.BookingStatus.CANCELLED " +
           "   AND bi.booking.checkInDate < :checkOutDate " +
           "   AND bi.booking.checkOutDate > :checkInDate" +
           ")")
    List<Room> findAvailableRooms(
            @Param("hotelId") Long hotelId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("roomType") RoomType roomType,
            @Param("minCapacity") Integer minCapacity
    );

    @Query("SELECT COUNT(r) = 0 FROM Room r " +
           "WHERE r.id = :roomId " +
           "AND r.id IN (" +
           "   SELECT bi.room.id FROM BookingItem bi " +
           "   WHERE bi.booking.bookingStatus <> com.stayease.entity.BookingStatus.CANCELLED " +
           "   AND bi.booking.checkInDate < :checkOutDate " +
           "   AND bi.booking.checkOutDate > :checkInDate" +
           ")")
    boolean isRoomAvailable(
            @Param("roomId") Long roomId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate
    );
}
