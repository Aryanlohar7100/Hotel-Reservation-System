package com.stayease.service;

import com.stayease.entity.Amenity;
import com.stayease.entity.HotelAmenity;
import java.util.List;

public interface AmenityService {
    List<Amenity> getAllAmenities();
    Amenity createAmenity(String name, String icon, String description);
    List<HotelAmenity> getHotelAmenities(Long hotelId);
    HotelAmenity attachAmenityToHotel(Long hotelId, Long amenityId, boolean isComplimentary, String details);
    void removeAmenityFromHotel(Long hotelId, Long amenityId);
}
