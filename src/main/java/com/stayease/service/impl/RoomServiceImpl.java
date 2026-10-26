package com.stayease.service.impl;

import com.stayease.dto.request.RoomCreateRequest;
import com.stayease.entity.Hotel;
import com.stayease.entity.Room;
import com.stayease.entity.RoomType;
import com.stayease.exception.InvalidBookingException;
import com.stayease.exception.ResourceNotFoundException;
import com.stayease.repository.HotelRepository;
import com.stayease.repository.RoomRepository;
import com.stayease.service.RoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    public RoomServiceImpl(RoomRepository roomRepository, HotelRepository hotelRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
    }

    @Override
    public List<Room> getRoomsByHotel(Long hotelId) {
        return roomRepository.findByHotelIdAndActiveTrue(hotelId);
    }

    @Override
    public List<Room> getAvailableRooms(Long hotelId, LocalDate checkIn, LocalDate checkOut, RoomType roomType, Integer minCapacity) {
        if (checkIn != null && checkOut != null) {
            if (!checkOut.isAfter(checkIn)) {
                throw new InvalidBookingException("Check-out date must be after check-in date");
            }
            return roomRepository.findAvailableRooms(hotelId, checkIn, checkOut, roomType, minCapacity);
        }
        return roomRepository.findByHotelIdAndActiveTrue(hotelId);
    }

    @Override
    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + id));
    }

    @Override
    @Transactional
    public Room createRoom(RoomCreateRequest request) {
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + request.getHotelId()));

        Room room = new Room();
        room.setHotel(hotel);
        room.setRoomNumber(request.getRoomNumber().trim());
        room.setRoomType(request.getRoomType());
        room.setBasePricePerNight(request.getBasePricePerNight());
        room.setCapacityAdults(request.getCapacityAdults() != null ? request.getCapacityAdults() : 2);
        room.setCapacityChildren(request.getCapacityChildren() != null ? request.getCapacityChildren() : 1);
        room.setAmenities(request.getAmenities() != null && !request.getAmenities().isBlank()
                ? request.getAmenities()
                : "Free High-Speed WiFi, Air Conditioning, Smart TV, En-suite Bathroom, Breakfast");
        room.setImageUrl(request.getImageUrl());
        room.setActive(true);

        return roomRepository.save(room);
    }

    @Override
    @Transactional
    public Room updateRoom(Long id, RoomCreateRequest request) {
        Room room = getRoomById(id);
        room.setRoomNumber(request.getRoomNumber().trim());
        room.setRoomType(request.getRoomType());
        room.setBasePricePerNight(request.getBasePricePerNight());
        if (request.getCapacityAdults() != null) {
            room.setCapacityAdults(request.getCapacityAdults());
        }
        if (request.getCapacityChildren() != null) {
            room.setCapacityChildren(request.getCapacityChildren());
        }
        if (request.getAmenities() != null) {
            room.setAmenities(request.getAmenities());
        }
        if (request.getImageUrl() != null && !request.getImageUrl().isBlank()) {
            room.setImageUrl(request.getImageUrl());
        }
        return roomRepository.save(room);
    }

    @Override
    @Transactional
    public void deleteRoom(Long id) {
        Room room = getRoomById(id);
        room.setActive(false);
        roomRepository.save(room);
    }
}
