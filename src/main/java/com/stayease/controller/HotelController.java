package com.stayease.controller;

import com.stayease.dto.request.HotelCreateRequest;
import com.stayease.dto.response.ApiResponse;
import com.stayease.entity.Hotel;
import com.stayease.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Hotel>>> searchHotels(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) String keyword) {
        List<Hotel> hotels = hotelService.searchHotels(city, minRating, keyword);
        return ResponseEntity.ok(ApiResponse.ok(hotels));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Hotel>> getHotelById(@PathVariable Long id) {
        Hotel hotel = hotelService.getHotelById(id);
        return ResponseEntity.ok(ApiResponse.ok(hotel));
    }

    @GetMapping("/cities")
    public ResponseEntity<ApiResponse<List<String>>> getCities() {
        List<String> cities = hotelService.getAvailableCities();
        return ResponseEntity.ok(ApiResponse.ok(cities));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Hotel>> createHotel(@Valid @RequestBody HotelCreateRequest request) {
        Hotel created = hotelService.createHotel(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Hotel added successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Hotel>> updateHotel(@PathVariable Long id, @Valid @RequestBody HotelCreateRequest request) {
        Hotel updated = hotelService.updateHotel(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Hotel updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.ok(ApiResponse.ok("Hotel deactivated successfully", null));
    }
}
