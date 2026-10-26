package com.stayease.service;

import com.stayease.dto.request.HotelCreateRequest;
import com.stayease.entity.Hotel;
import java.util.List;

public interface HotelService {
    List<Hotel> getAllActiveHotels();
    List<Hotel> searchHotels(String city, Double minRating, String keyword);
    Hotel getHotelById(Long id);
    Hotel createHotel(HotelCreateRequest request);
    Hotel updateHotel(Long id, HotelCreateRequest request);
    void deleteHotel(Long id);
    List<String> getAvailableCities();
}
