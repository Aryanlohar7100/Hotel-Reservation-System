package com.stayease.service;

import com.stayease.dto.request.RoomCreateRequest;
import com.stayease.entity.Room;
import com.stayease.entity.RoomType;
import java.time.LocalDate;
import java.util.List;

public interface RoomService {
    List<Room> getRoomsByHotel(Long hotelId);
    List<Room> getAvailableRooms(Long hotelId, LocalDate checkIn, LocalDate checkOut, RoomType roomType, Integer minCapacity);
    Room getRoomById(Long id);
    Room createRoom(RoomCreateRequest request);
    Room updateRoom(Long id, RoomCreateRequest request);
    void deleteRoom(Long id);
}
