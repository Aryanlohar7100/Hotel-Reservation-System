package com.stayease.controller;

import com.stayease.dto.response.ApiResponse;
import com.stayease.entity.Amenity;
import com.stayease.entity.HotelAmenity;
import com.stayease.service.AmenityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/amenities")
public class AmenityController {

    private final AmenityService amenityService;

    public AmenityController(AmenityService amenityService) {
        this.amenityService = amenityService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Amenity>>> getAllAmenities() {
        return ResponseEntity.ok(ApiResponse.ok(amenityService.getAllAmenities()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Amenity>> createAmenity(
            @RequestParam String name,
            @RequestParam(required = false) String icon,
            @RequestParam(required = false) String description) {
        Amenity amenity = amenityService.createAmenity(name, icon, description);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Amenity created successfully", amenity));
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<ApiResponse<List<HotelAmenity>>> getHotelAmenities(@PathVariable Long hotelId) {
        return ResponseEntity.ok(ApiResponse.ok(amenityService.getHotelAmenities(hotelId)));
    }

    @PostMapping("/hotel/{hotelId}/{amenityId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<HotelAmenity>> attachAmenity(
            @PathVariable Long hotelId,
            @PathVariable Long amenityId,
            @RequestParam(defaultValue = "true") boolean complimentary,
            @RequestParam(required = false) String details) {
        HotelAmenity ha = amenityService.attachAmenityToHotel(hotelId, amenityId, complimentary, details);
        return ResponseEntity.ok(ApiResponse.ok("Amenity attached to hotel", ha));
    }

    @DeleteMapping("/hotel/{hotelId}/{amenityId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> removeAmenity(
            @PathVariable Long hotelId,
            @PathVariable Long amenityId) {
        amenityService.removeAmenityFromHotel(hotelId, amenityId);
        return ResponseEntity.ok(ApiResponse.ok("Amenity removed from hotel", null));
    }
}
