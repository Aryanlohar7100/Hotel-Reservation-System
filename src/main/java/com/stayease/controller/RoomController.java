package com.stayease.controller;

import com.stayease.dto.request.RoomCreateRequest;
import com.stayease.dto.response.ApiResponse;
import com.stayease.entity.Room;
import com.stayease.entity.RoomType;
import com.stayease.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<ApiResponse<List<Room>>> getRoomsByHotel(@PathVariable Long hotelId) {
        List<Room> rooms = roomService.getRoomsByHotel(hotelId);
        return ResponseEntity.ok(ApiResponse.ok(rooms));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<Room>>> getAvailableRooms(
            @RequestParam Long hotelId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam(required = false) RoomType roomType,
            @RequestParam(required = false) Integer minCapacity) {
        List<Room> rooms = roomService.getAvailableRooms(hotelId, checkIn, checkOut, roomType, minCapacity);
        return ResponseEntity.ok(ApiResponse.ok(rooms));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Room>> getRoomById(@PathVariable Long id) {
        Room room = roomService.getRoomById(id);
        return ResponseEntity.ok(ApiResponse.ok(room));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Room>> createRoom(@Valid @RequestBody RoomCreateRequest request) {
        Room created = roomService.createRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Room created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Room>> updateRoom(@PathVariable Long id, @Valid @RequestBody RoomCreateRequest request) {
        Room updated = roomService.updateRoom(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Room updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok(ApiResponse.ok("Room deactivated successfully", null));
    }
}
